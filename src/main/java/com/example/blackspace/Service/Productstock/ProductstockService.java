package com.example.blackspace.Service.Productstock;

import com.example.blackspace.Model.Products.Productstock;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductstockService {


    List<Productstock> getAllProductstock();

    // User saveproductstock(Productstockser productstock);
    Productstock addProductstock(Productstock productstock, MultipartFile[] imageurl, MultipartFile audiourl) throws IOException;

    Productstock getProductstockById(Long id);
    Productstock updateProductstock(Productstock productstock,MultipartFile[] imageurl, MultipartFile audiourl) throws IOException;

    void deleteProduct(Long id); //


    Productstock findById(Long id);

    Productstock saveProduct(Productstock product); // <-- this saves updates


    long countByUsername(String username);


    long countAllProducts();
    Map<String, Long> countProductsByCategory();

    List<Productstock> getProductsByUsername(String username);


    Map<String, Long> countProductsByUsername(String username);



    List<Productstock> getAllProducts();

    List<Productstock> getProductsFromActiveStores();

    // Add this method
    List<Productstock> searchProducts(String query);


    List<Productstock> findSimilarProducts(String category, Long excludeId, int limit);








}
