package com.example.blackspace.Chatbot;

import com.example.blackspace.Model.Category;
import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Repository.user.ProductsRepository;
import com.example.blackspace.Service.Category.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ChatbotService {

    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class);

    private final ProductsRepository productsRepository;
    private final CategoryService categoryService;

    public ChatbotService(ProductsRepository productsRepository, CategoryService categoryService) {
        this.productsRepository = productsRepository;
        this.categoryService = categoryService;
    }

    public ChatbotResponse processMessage(String message) {
        if (message == null || message.isBlank()) {
            return greeting();
        }

        String lower = message.toLowerCase().trim();

        // Greeting detection
        if (isGreeting(lower)) {
            return greeting();
        }

        // Help / what can you do
        if (lower.contains("help") || lower.contains("what can you do") || lower.contains("how do i")) {
            return helpResponse();
        }

        // Category browsing
        if (lower.contains("categories") || lower.contains("category") || lower.equals("browse")) {
            return categoryList();
        }

        // Trending / popular
        if (lower.contains("trending") || lower.contains("popular") || lower.contains("best") || lower.contains("top")) {
            return trendingProducts();
        }

        // Latest / new
        if (lower.contains("latest") || lower.contains("new") || lower.contains("recent")) {
            return latestProducts();
        }

        // Default: search products
        return searchProducts(message.trim());
    }

    public ChatbotResponse processImageLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            return new ChatbotResponse(
                    "I couldn't identify anything in that image. Try describing what you're looking for instead.",
                    List.of(), List.of("Show me trending products", "Browse categories")
            );
        }

        // Search using each label and collect results
        Set<Long> seenIds = new HashSet<>();
        List<ProductResult> results = new ArrayList<>();

        for (String label : labels) {
            List<Productstock> found = productsRepository.searchAllFields(label);
            for (Productstock p : found) {
                if (seenIds.add(p.getId())) {
                    results.add(toResult(p));
                    if (results.size() >= 8) break;
                }
            }
            if (results.size() >= 8) break;
        }

        String labelStr = String.join(", ", labels);
        if (results.isEmpty()) {
            return new ChatbotResponse(
                    "I detected: **" + labelStr + "** in your image, but couldn't find matching products. Try different keywords!",
                    List.of(), List.of("Show me trending products", "Browse categories")
            );
        }

        return new ChatbotResponse(
                "I detected **" + labelStr + "** in your image. Here's what I found:",
                results, List.of()
        );
    }

    private ChatbotResponse searchProducts(String query) {
        // Try multi-field search
        List<Productstock> results = productsRepository.searchAllFields(query);

        if (results.isEmpty()) {
            // Try splitting into individual words
            String[] words = query.split("\\s+");
            Set<Long> seenIds = new HashSet<>();
            List<Productstock> combined = new ArrayList<>();
            for (String word : words) {
                if (word.length() < 3) continue;
                for (Productstock p : productsRepository.searchAllFields(word)) {
                    if (seenIds.add(p.getId())) {
                        combined.add(p);
                    }
                }
            }
            results = combined;
        }

        if (results.isEmpty()) {
            return new ChatbotResponse(
                    "I couldn't find products matching \"" + query + "\". Try different keywords or browse categories.",
                    List.of(),
                    List.of("Browse categories", "Show me trending products", "Latest products")
            );
        }

        List<ProductResult> products = results.stream()
                .limit(8)
                .map(this::toResult)
                .toList();

        return new ChatbotResponse(
                "Here's what I found for \"" + query + "\":",
                products, List.of()
        );
    }

    private ChatbotResponse greeting() {
        return new ChatbotResponse(
                "Hi! I'm the **BlackSpace AI Assistant**. I can help you find products by text, voice, or image. What are you looking for today?",
                List.of(),
                List.of("Show me trending products", "Latest products", "Browse categories")
        );
    }

    private ChatbotResponse helpResponse() {
        return new ChatbotResponse(
                "Here's what I can do:\n\n" +
                "- **Text search** — Type what you're looking for (e.g. \"red shoes\", \"electronics\")\n" +
                "- **Voice search** — Click the microphone and speak\n" +
                "- **Image search** — Upload a photo and I'll find similar products\n\n" +
                "Try it out!",
                List.of(),
                List.of("Show me trending products", "Browse categories", "Latest products")
        );
    }

    private ChatbotResponse categoryList() {
        List<Category> categories = categoryService.getAllCategories();
        List<String> suggestions = categories.stream()
                .limit(6)
                .map(Category::getName)
                .toList();

        return new ChatbotResponse(
                "Here are our product categories. Tap one to browse:",
                List.of(), suggestions
        );
    }

    private ChatbotResponse trendingProducts() {
        List<Productstock> all = productsRepository.findAllByActiveStoreOrderByCreatedAtDesc();
        List<ProductResult> top = all.stream()
                .sorted(Comparator.comparingInt((Productstock p) ->
                        (p.getLikes() != null ? p.getLikes() : 0)).reversed())
                .limit(6)
                .map(this::toResult)
                .toList();

        if (top.isEmpty()) {
            return new ChatbotResponse("No trending products right now. Check back later!", List.of(), List.of());
        }

        return new ChatbotResponse("Here are the trending products:", top, List.of());
    }

    private ChatbotResponse latestProducts() {
        List<Productstock> latest = productsRepository.findAllByActiveStoreOrderByCreatedAtDesc();
        List<ProductResult> results = latest.stream()
                .limit(6)
                .map(this::toResult)
                .toList();

        if (results.isEmpty()) {
            return new ChatbotResponse("No products available yet. Check back soon!", List.of(), List.of());
        }

        return new ChatbotResponse("Here are the latest products:", results, List.of());
    }

    private ProductResult toResult(Productstock p) {
        String image = "default.png";
        if (p.getImageurls() != null && !p.getImageurls().isEmpty()) {
            image = p.getImageurls().split(",")[0];
        }
        return new ProductResult(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getCurrency(),
                "/uploads/products/images/" + image,
                p.getCategory()
        );
    }

    private boolean isGreeting(String msg) {
        return msg.matches("^(hi|hello|hey|howdy|good morning|good afternoon|good evening|yo|sup|hola|greetings).*");
    }

    // Response DTOs
    public record ChatbotResponse(String message, List<ProductResult> products, List<String> suggestions) {}
    public record ProductResult(Long id, String name, String price, String currency, String image, String category) {}
}
