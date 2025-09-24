package com.project.webscraping.repository;

import com.project.webscraping.model.ScrapedData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScrapedDataRepository extends MongoRepository<ScrapedData, String> {
    
    List<ScrapedData> findByCategoryOrderByScrapedAtDesc(String category);
    
    List<ScrapedData> findTop10ByCategoryOrderByScrapedAtDesc(String category);
    
    @Query("{'scrapedAt': {$gte: ?0}}")
    List<ScrapedData> findByScrapedAtAfter(LocalDateTime date);
    
    long countByCategory(String category);
    
    // Duplicate kontrolü için
    boolean existsByTitleAndCategory(String title, String category);
}

