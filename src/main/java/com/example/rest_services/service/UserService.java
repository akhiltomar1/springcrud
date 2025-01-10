package com.example.rest_services.service;

import com.example.rest_services.model.User;
import com.example.rest_services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user); // No password hashing!
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isAdmin(String username) {
        return "admin".equals(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


}