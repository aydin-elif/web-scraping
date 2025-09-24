package com.project.webscraping.controller;

import com.project.webscraping.dto.LoginRequest;
import com.project.webscraping.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping({"/", "/login"})
    public String loginPage(Model model, HttpSession session) {
        // Eğer zaten giriş yapmışsa dashboard'a yönlendir
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, 
                       HttpSession session, 
                       RedirectAttributes redirectAttributes,
                       Model model) {
        try {
            boolean isAuthenticated = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            
            if (isAuthenticated) {
                session.setAttribute("user", loginRequest.getUsername());
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Kullanıcı adı veya şifre hatalı!");
                model.addAttribute("loginRequest", new LoginRequest());
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Giriş yapılırken bir hata oluştu!");
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}

