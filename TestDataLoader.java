// Bunun amacı uygulama açılır açılmaz MongoDB'ye otomatik test verisi göndermek
// Kayıt eklenirse bağlantı tamam
// Hata alınırsa bağlantı / config / Docker problemli

package com.project.webscraping;

import com.project.webscraping.domain.User;
import com.project.webscraping.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//CommandLineRunner --> Uygulama açıldığında run() metodu otomatik çalışır 
@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public TestDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User u = new User();
        u.setUsername("testuser");
        u.setPassword("1234");
        userRepository.save(u); // MongoDB’ye kaydediyor
        System.out.println("User saved to MongoDB!");
    }
}
