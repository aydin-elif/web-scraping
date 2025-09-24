package com.project.webscraping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        String username = (String) session.getAttribute("user");
        model.addAttribute("username", username);
        
        // Şimdilik sabit değerler - sonra ScrapingService'ten alacağız
        model.addAttribute("constructionCount", 0);
        model.addAttribute("healthCount", 0);
        model.addAttribute("technologyCount", 0);
        
        return "dashboard";
    }
    
    @GetMapping("/category/{categoryName}")
    public String categoryDetail(@PathVariable String categoryName, 
                                Model model, 
                                HttpSession session) {
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        // Kategori validasyonu
        if (!isValidCategory(categoryName)) {
            return "redirect:/dashboard";
        }
        
        String username = (String) session.getAttribute("user");
        model.addAttribute("username", username);
        
        // Şimdilik boş liste - sonra ScrapingService'ten alacağız
        model.addAttribute("data", java.util.Collections.emptyList());
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("categoryTitle", getCategoryTitle(categoryName));
        
        return "category-detail";
    }
    
    private boolean isValidCategory(String category) {
        return "construction".equals(category) || 
               "health".equals(category) || 
               "technology".equals(category);
    }
    
    private String getCategoryTitle(String category) {
        switch (category) {
            case "construction": return "İnşaat Sektörü";
            case "health": return "Sağlık Sektörü";
            case "technology": return "Teknoloji Sektörü";
            default: return "Bilinmeyen Kategori";
        }
    }
}

