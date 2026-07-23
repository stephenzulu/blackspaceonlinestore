package com.example.blackspace.Service.Store;


import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;

import com.example.blackspace.Repository.user.ProductsRepository;
import com.example.blackspace.Repository.user.StoreRepository;
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
import java.util.Iterator;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private static final String UPLOAD_DIR = "uploads/stores/";



    private final StoreRepository storeRepository;
    private final ProductsRepository productstockRepository;

    public StoreServiceImpl(StoreRepository storeRepository,
                            ProductsRepository productstockRepository) {
        this.storeRepository = storeRepository;
        this.productstockRepository = productstockRepository;
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> getActiveStores() {
        return storeRepository.findByActiveTrue();
    }


    @Override
    public Store getStoreByStoreid(String storeid) {
        return storeRepository.findByStoreid(storeid);
    }

    @Override
    public Store getStoreByid(Long id) {
        return storeRepository.findByid(id);
    }

    @Override
    public List<Productstock> getProductsByStore(String storeid) {
        //  DIRECT QUERY
        return productstockRepository.findByStoreid(storeid);
    }


    @Override
    public Store findById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
    }

    @Override
    public void saveStore(Store store) {
        storeRepository.save(store);
    }

    @Override
    public String saveImage(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            String originalName = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "_" + originalName;

            // Try to compress JPEG/PNG images
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
                        fileName = System.currentTimeMillis() + "_" + originalName.replaceAll("\\.[^.]+$", "") + ".jpg";
                        File outputFile = new File(UPLOAD_DIR + fileName);
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
                    // Fall back to saving original if compression fails
                }
            }

            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image file", e);
        }
    }


    @Override
    public Store getStoreById(Long id) {
        return storeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }


    @Override
    public Store getStoreByUsername(String username) {
        List<Store> stores = storeRepository.findByUsername(username);
        return stores.isEmpty() ? null : stores.get(0);
    }

    @Override
    public void updateStore(Store store) {
        storeRepository.save(store);
    }















}
