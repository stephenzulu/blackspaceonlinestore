package com.example.blackspace.Service.Store;


import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;

import com.example.blackspace.Repository.user.ProductsRepository;
import com.example.blackspace.Repository.user.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR, fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName; // save this in DB
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
        return storeRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void updateStore(Store store) {
        storeRepository.save(store);
    }















}
