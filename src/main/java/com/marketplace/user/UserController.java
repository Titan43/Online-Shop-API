package com.marketplace.user;

import com.marketplace.user.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    private final IUserService userService;

    @GetMapping(path = "{userID}")
    public Optional<User> getUser(@PathVariable("userID") String id){
        return userService.getUser(id);
    }

    @Autowired
    public UserController(@Qualifier("firstImplementation") IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED, reason = "User was successfully created(CODE 201)")
    public void registerNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userID}")
    @ResponseStatus(code = HttpStatus.OK, reason = "User was successfully deleted(CODE 200)")
    public void deleteUser(@PathVariable("userID") String id){
        userService.deleteUser(id);
    }
}
