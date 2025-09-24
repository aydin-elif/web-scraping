
package com.project.webscraping.controller;

import com.project.webscraping.service.ScrapingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;


@Controller
public class DashboardController {

	@Autowired  // ← Buraya yazın 
	private ScrapingService scrapingService;

    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Session kontrolü
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        String username = (String) session.getAttribute("user");
        model.addAttribute("username", username);
        
     // Eski satırlar:
/*
        model.addAttribute("constructionCount", 0);
        model.addAttribute("healthCount", 0);
        model.addAttribute("technologyCount", 0);
*/
        // Yeni satırlar:
        model.addAttribute("constructionCount", scrapingService.getDataCountByCategory("construction"));
        model.addAttribute("healthCount", scrapingService.getDataCountByCategory("health"));
        model.addAttribute("technologyCount", scrapingService.getDataCountByCategory("technology"));


        
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
        //model.addAttribute("data", java.util.Collections.emptyList());
        
        // ScrapingService'den gerçek haberleri çekiyor
        model.addAttribute("data", scrapingService.getDataByCategory(categoryName));

        
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

