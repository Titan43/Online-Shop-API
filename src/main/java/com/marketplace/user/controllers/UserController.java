package com.marketplace.user.controllers;
import com.marketplace.user.User;
import com.marketplace.user.userService.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.marketplace.constants.IAPIConstants.API_PREFIX;

@RestController
@RequestMapping(path=API_PREFIX+"user")
@AllArgsConstructor
public class UserController {

    @Autowired
    private final IUserService userService;

    @GetMapping
    public User getUser(@RequestParam String username, Principal principal){
        return userService.getUser(username, principal);
    }

    @PostMapping(path = "/register")
    @ResponseStatus(code = HttpStatus.CREATED, reason = "User was successfully created(CODE 201)")
    public void registerNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK, reason = "User was successfully deleted(CODE 200)")
    public void deleteUser(@RequestParam String username, Principal principal){
        userService.deleteUser(username, principal);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK, reason = "User was successfully updated(CODE 200)")
    public void updateUser(@RequestParam String username, @RequestBody User user, Principal principal){
        userService.updateUser(username, user, principal);
    }

}
