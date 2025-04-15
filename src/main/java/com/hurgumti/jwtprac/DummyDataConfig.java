package com.hurgumti.jwtprac;

import com.hurgumti.jwtprac.entity.User;
import com.hurgumti.jwtprac.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DummyDataConfig {

    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("testuser").isEmpty()) {
                System.out.println("Creating dummy user");
                userRepository.save(User.builder().username("testuser").password(new BCryptPasswordEncoder().encode("password123")).role("ROLE_USER").build());
            }
        };
    }
}
