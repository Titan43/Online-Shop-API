package com.marketplace.security;

import com.marketplace.user.User;
import com.marketplace.user.UserRepository;
import com.marketplace.user.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class SecurityConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            User testUser = new User("testName", "testSurname", "testUser", "testeml@email.com",
                    LocalDate.of(2000, 1, 1), "*101#", UserRole.BUYER);

            userRepository.save(testUser);
        };
    }
}
