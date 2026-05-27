package com.example.blackspace.Service.Store;


import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    Store getStoreByStoreid(String storeid);

    Store getStoreByid(Long id);



    List<Productstock> getProductsByStore(String storeid);

    Store findById(Long id);


    List<Store> getAllStores();


    Store getStoreById(Long id);
    void saveStore(Store store);
    void deleteStore(Long id);
    Store getStoreByUsername(String username);



    String saveImage(MultipartFile file);

    void updateStore(Store store);





}

