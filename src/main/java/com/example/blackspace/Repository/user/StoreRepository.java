package com.example.blackspace.Repository.user;


import com.example.blackspace.Model.Stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByStoreid(String storeid);

    Store findByid(Long id);

    Optional<Store> findByUsername(String username);

    List<Store> findByActiveTrue();

}
