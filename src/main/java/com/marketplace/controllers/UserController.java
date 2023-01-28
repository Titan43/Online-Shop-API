package com.marketplace.controllers;
import com.marketplace.user.User;
import com.marketplace.user.userService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.marketplace.constants.APIConstants.API_PREFIX;

@RestController
@RequestMapping(path=API_PREFIX+"user")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser(@RequestParam String username, Principal principal){
        return userService.getUser(username, principal);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerNewUser(@RequestBody User user){
        return userService.addNewUser(user);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestParam String username, Principal principal){
        return userService.deleteUser(username, principal);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestParam String username, @RequestBody User user, Principal principal){
        return userService.updateUser(username, user, principal);
    }
}
