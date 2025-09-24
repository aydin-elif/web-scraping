package com.project.webscraping.service;

import com.project.webscraping.model.User;
import com.project.webscraping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void initDefaultUser() {
        // Varsayılan kullanıcı oluştur (sadece test için)
        if (!userRepository.existsByUsername("admin")) {
            User defaultUser = new User();
            defaultUser.setUsername("admin");
            defaultUser.setPassword(passwordEncoder.encode("12345"));
            defaultUser.setEmail("admin@example.com");
            defaultUser.setEnabled(true);
            userRepository.save(defaultUser);
            
            System.out.println("Varsayılan kullanıcı oluşturuldu: admin / 12345");
        }
    }
    
    public boolean authenticate(String username, String password) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElse(null);
            
            if (user != null && user.isEnabled()) {
                return passwordEncoder.matches(password, user.getPassword());
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    public User createUser(String username, String password, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Kullanıcı adı zaten mevcut!");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setEnabled(true);
        
        return userRepository.save(user);
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}

