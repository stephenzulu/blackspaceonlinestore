package com.example.blackspace.Service.User;


import com.example.blackspace.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User saveUser(User user);

    boolean emailExists(String email);

    boolean phoneExists(String phoneNumber);

    void deleteUser(Long id);

    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByPhoneNumber(String phonenumber);
    Optional<User> getUserByEmail(String email);

    User findByUsername(String username);












}

