package com.marketplace.security;

import com.marketplace.product.Product;
import com.marketplace.product.productService.ProductRepository;
import com.marketplace.user.User;
import com.marketplace.user.userService.UserRepository;
import com.marketplace.user.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import java.util.List;

import static com.marketplace.constants.IAPIConstants.*;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig{

    @Autowired
    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(AUTH_PATH)
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(PRODUCTS_PATH)
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(REGISTER_PATH)
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .authenticationProvider(authenticationProvider())
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, ProductRepository productRepository){
        return args -> {
            User testUser = new User("testName", "testSurname",
                    "testUser", "$2a$12$zKMfr864c/E2mCMWT5rMt.9YR3RzZnwhrryk1p6tnVExDXIZRPUbW",
                    "testeml@email.com",
                    LocalDate.of(2000, 1, 1), "*101#", UserRole.BUYER);
            User testUser2 = new User("test2", "test2",
                    "test2", "$2a$12$zKMfr864c/E2mCMWT5rMt.9YR3RzZnwhrryk1p6tnVExDXIZRPUbW",
                    "test@email.com",
                    LocalDate.of(2000, 1, 1), "*101#", UserRole.BUYER);
            Product product1 = new Product("testing", 9.99, 10L, "test", testUser);

            userRepository.saveAll(List.of(testUser, testUser2));
            productRepository.saveAll(List.of(product1));
        };
    }
}
