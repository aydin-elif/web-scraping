package com.project.webscraping.controller;

import com.project.webscraping.model.User;
import com.project.webscraping.service.AuthService;
import com.project.webscraping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("🔍 AdminController: /admin/users çağrıldı");
        
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            System.out.println("⚠️ Session bulunamadı, login'e yönlendiriliyor");
            return "redirect:/login";
        }
        
        try {
            List<User> users = userService.getAllUsers();
            System.out.println("✅ " + users.size() + " kullanıcı bulundu");
            
            model.addAttribute("users", users);
            model.addAttribute("username", session.getAttribute("user"));
            
            return "admin/users";
        } catch (Exception e) {
            System.err.println("❌ AdminController hatası: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Kullanıcılar yüklenirken hata: " + e.getMessage());
            return "error";
        }
    }
    
    @PostMapping("/users/create")
    @ResponseBody
    public String createUser(@RequestParam String username, 
                            @RequestParam String password, 
                            @RequestParam String email,
                            HttpSession session) {
        
        System.out.println("🔍 Yeni kullanıcı oluşturma: " + username);
        
        if (session.getAttribute("user") == null) {
            return "unauthorized";
        }
        
        try {
            if (userService.existsByUsername(username)) {
                return "error: Kullanıcı adı zaten mevcut!";
            }
            
            authService.createUser(username, password, email);
            System.out.println("✅ Kullanıcı oluşturuldu: " + username);
            return "success";
            
        } catch (Exception e) {
            System.err.println("❌ Kullanıcı oluşturma hatası: " + e.getMessage());
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
