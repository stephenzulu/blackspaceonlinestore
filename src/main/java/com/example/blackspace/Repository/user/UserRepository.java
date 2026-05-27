package com.example.blackspace.Repository.user;

import com.example.blackspace.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phonenumber);

    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);





}
