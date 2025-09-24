package com.project.webscraping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebScrapingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebScrapingApplication.class, args);

		System.out.println("\n" + "=".repeat(60));
		System.out.println("🚀 AI NEWS SCRAPER BAŞLADI!");
		System.out.println("📍 http://localhost:8100");
		System.out.println("👤 Demo Kullanıcı: admin / 12345");
		System.out.println("=".repeat(60) + "\n");
	}

}
