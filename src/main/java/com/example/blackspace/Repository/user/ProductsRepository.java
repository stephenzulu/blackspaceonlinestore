package com.example.blackspace.Repository.user;

import com.example.blackspace.Model.Products.Productstock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Productstock, Long> {

    Productstock findByName(String name);

    List<Productstock> findByStoreid(String storeid);

    List<Productstock> findByUsername(String username);

    long countByUsername(String username);





    // Search by product stock name
    List<Productstock> findByNameContainingIgnoreCase(String name);




    List<Productstock> findByCategoryIgnoreCase(String category);

    @Query("SELECT p FROM Productstock p WHERE p.username IN (SELECT s.username FROM Store s WHERE s.active = true)")
    List<Productstock> findAllByActiveStore();











}
