package com.marketplace.user.userService;

import com.marketplace.user.userEntities.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface UserService {
    ResponseEntity<String> addNewUser(User user);

    ResponseEntity<String> deleteUser(String username, Principal principal);

    ResponseEntity<?> getUser(String username, Principal principal);

    ResponseEntity<String> updateUser(String username, User user, Principal principal);
}