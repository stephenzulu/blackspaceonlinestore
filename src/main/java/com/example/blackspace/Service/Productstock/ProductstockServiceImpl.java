package com.example.blackspace.Service.Productstock;

import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Repository.user.ProductsRepository;
import com.example.blackspace.Service.Store.StoreService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
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

        // Save multiple images (compressed)
        if (images != null && images.length > 0) {
            StringBuilder filenames = new StringBuilder();
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String savedName = compressAndSaveImage(image, imagePath);
                    filenames.append(savedName).append(",");
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

        // Save new images (compressed) and append
        if (newImages != null && newImages.length > 0) {
            for (MultipartFile image : newImages) {
                if (!image.isEmpty()) {
                    String savedName = compressAndSaveImage(image, imagePath);
                    if (filenames.length() > 0 && !filenames.toString().endsWith(",")) {
                        filenames.append(",");
                    }
                    filenames.append(savedName);
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



    private String compressAndSaveImage(MultipartFile file, Path targetDir) throws IOException {
        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();

        if (contentType != null && contentType.startsWith("image/")) {
            try {
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                if (originalImage != null) {
                    // Resize if larger than 1200px
                    int maxDim = 1200;
                    int w = originalImage.getWidth();
                    int h = originalImage.getHeight();
                    if (w > maxDim || h > maxDim) {
                        double scale = Math.min((double) maxDim / w, (double) maxDim / h);
                        int newW = (int) (w * scale);
                        int newH = (int) (h * scale);
                        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
                        Graphics2D g = resized.createGraphics();
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.drawImage(originalImage, 0, 0, newW, newH, null);
                        g.dispose();
                        originalImage = resized;
                    }

                    // Save as compressed JPEG
                    String fileName = UUID.randomUUID() + "_" + originalName.replaceAll("\\.[^.]+$", "") + ".jpg";
                    File outputFile = targetDir.resolve(fileName).toFile();
                    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                    if (writers.hasNext()) {
                        ImageWriter writer = writers.next();
                        ImageWriteParam param = writer.getDefaultWriteParam();
                        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                        param.setCompressionQuality(0.75f);
                        ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile);
                        writer.setOutput(ios);
                        writer.write(null, new IIOImage(originalImage, null, null), param);
                        ios.close();
                        writer.dispose();
                        return fileName;
                    }
                }
            } catch (Exception e) {
                // Fall back to saving original
            }
        }

        // Fallback: save original file
        String fileName = UUID.randomUUID() + "_" + originalName;
        Files.copy(file.getInputStream(), targetDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
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
