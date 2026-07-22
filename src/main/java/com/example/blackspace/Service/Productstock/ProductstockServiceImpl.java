package com.example.blackspace.Service.Productstock;

import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Repository.user.ProductsRepository;
import com.example.blackspace.Service.Store.StoreService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class ProductstockServiceImpl implements ProductstockService {


    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductsRepository productsRepository;


    private final String IMAGE_DIR = new File("uploads/products/images/").getAbsolutePath() + "/";
    private final String DOC_DIR = new File("uploads/products/audio/").getAbsolutePath() + "/";


    @Override
    public List<Productstock> getAllProductstock() {
        return productsRepository.findAllByOrderByCreatedAtDesc();
    }


    @Override
    public Productstock addProductstock(Productstock productstock, MultipartFile[] images, MultipartFile audiourl) throws IOException {

        Path imagePath = Paths.get(IMAGE_DIR);
        Path docPath = Paths.get(DOC_DIR);
        Files.createDirectories(imagePath);
        Files.createDirectories(docPath);

        // Save multiple images
        if (images != null && images.length > 0) {
            StringBuilder filenames = new StringBuilder();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                    Files.copy(image.getInputStream(), imagePath.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
                    filenames.append(imageName).append(",");
                }
            }
            // Remove trailing comma
            productstock.setImageurls(filenames.length() > 0 ? filenames.substring(0, filenames.length() - 1) : null);
        }

        // Save audio
        if (audiourl != null && !audiourl.isEmpty()) {
            String audioName = UUID.randomUUID() + "_" + audiourl.getOriginalFilename();
            Files.copy(audiourl.getInputStream(), docPath.resolve(audioName), StandardCopyOption.REPLACE_EXISTING);
            productstock.setAudiourl(audioName);
        }




        return productsRepository.save(productstock);
    }


    @Override
    public Productstock getProductstockById(Long id) {
        return productsRepository.findById(id).orElse(null);
    }





    @Override
    public Productstock updateProductstock(Productstock productstock, MultipartFile[] newImages, MultipartFile audiourl) throws IOException {
        Path imagePath = Paths.get(IMAGE_DIR);
        Path docPath = Paths.get(DOC_DIR);
        Files.createDirectories(imagePath);
        Files.createDirectories(docPath);

        // Start with existing images
        String existingImages = productstock.getImageurls() != null ? productstock.getImageurls() : "";
        StringBuilder filenames = new StringBuilder(existingImages);

        // Save new images and append
        if (newImages != null && newImages.length > 0) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                    Files.copy(image.getInputStream(), imagePath.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
                    if (filenames.length() > 0 && !filenames.toString().endsWith(",")) {
                        filenames.append(",");
                    }
                    filenames.append(imageName);
                }
            }
        }

        productstock.setImageurls(filenames.length() > 0 ? filenames.toString() : null);

        // Save audio if provided
        if (audiourl != null && !audiourl.isEmpty()) {
            String audioName = UUID.randomUUID() + "_" + audiourl.getOriginalFilename();
            Files.copy(audiourl.getInputStream(), docPath.resolve(audioName), StandardCopyOption.REPLACE_EXISTING);
            productstock.setAudiourl(audioName);
        }

        return productsRepository.save(productstock);
    }



    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (productsRepository.existsById(id)) {
            productsRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with ID: " + id);
        }
    }



    @Override
    public Productstock findById(Long id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }



    @Override
    public Productstock saveProduct(Productstock product) {
        return productsRepository.save(product); // saves updates to DB
    }


    @Override
    public long countByUsername(String username) {
        return productsRepository.countByUsername(username);
    }


    @Override
    public long countAllProducts() {
        return productsRepository.count();
    }
    @Override
    public Map<String, Long> countProductsByCategory() {

        List<Productstock> products = productsRepository.findAll();

        Map<String, Long> categoryCount = new HashMap<>();

        for (Productstock p : products) {
            String category = p.getCategory();
            categoryCount.put(category,
                    categoryCount.getOrDefault(category, 0L) + 1);
        }

        return categoryCount;
    }

    @Override
    public List<Productstock> getProductsByUsername(String username) {
        return productsRepository.findByUsernameOrderByCreatedAtDesc(username);
    }


    @Override
    public Map<String, Long> countProductsByUsername(String username) {

        List<Productstock> products =
                productsRepository.findByUsername(username);

        Map<String, Long> categoryCount = new HashMap<>();

        for (Productstock p : products) {
            String category = p.getCategory();
            categoryCount.put(category,
                    categoryCount.getOrDefault(category, 0L) + 1);
        }

        return categoryCount;
    }



    @Override
    public List<Productstock> getAllProducts() {
        return productsRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Productstock> getProductsFromActiveStores() {
        return productsRepository.findAllByActiveStoreOrderByCreatedAtDesc();
    }

    @Override
    public List<Productstock> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return getAllProducts();
        }
        return productsRepository.findByNameContainingIgnoreCase(query);
    }




    @Override
    public List<Productstock> findSimilarProducts(String category, Long excludeId, int limit) {
        return productsRepository.findByCategoryIgnoreCase(category)
                .stream()
                .filter(p -> !p.getId().equals(excludeId))
                .limit(limit)
                .toList();
    }



}
