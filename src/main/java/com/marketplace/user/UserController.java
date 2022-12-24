package com.marketplace.user;

import com.marketplace.user.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(@Qualifier("firstImplementation") IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
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
