package com.project.webscraping.controller;

import com.project.webscraping.model.User;
import com.project.webscraping.service.AuthService;
import com.project.webscraping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("username", session.getAttribute("user"));
        
        return "admin/users";
    }
    
    @PostMapping("/users/create")
    @ResponseBody
    public String createUser(@RequestParam String username, 
                            @RequestParam String password, 
                            @RequestParam String email,
                            HttpSession session) {
        
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            return "unauthorized";
        }
        
        try {
            if (userService.existsByUsername(username)) {
                return "error: Kullanıcı adı zaten mevcut!";
            }
            
            authService.createUser(username, password, email);
            return "success";
            
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
    
    @GetMapping("/database-info")
    @ResponseBody
    public String getDatabaseInfo(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "unauthorized";
        }
        
        try {
            long userCount = userService.getUserCount();
            List<User> users = userService.getAllUsers();
            
            StringBuilder info = new StringBuilder();
            info.append("=== VERİTABANI BİLGİLERİ ===\n");
            info.append("Toplam Kullanıcı Sayısı: ").append(userCount).append("\n\n");
            info.append("Kullanıcılar:\n");
            
            for (User user : users) {
                info.append("- Username: ").append(user.getUsername())
                    .append(", Email: ").append(user.getEmail())
                    .append(", Oluşturulma: ").append(user.getCreatedAt())
                    .append("\n");
            }
            
            return info.toString();
        } catch (Exception e) {
            return "Veritabanı bağlantı hatası: " + e.getMessage();
        }
    }
}

