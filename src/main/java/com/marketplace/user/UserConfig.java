package com.marketplace.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {
            User testUser = new User("testName", "testSurname", "testeml@email.com",
                    LocalDate.of(2000, 1, 1), "*101#", UserRole.BUYER);

            userRepository.save(testUser);
        };
    }
}
