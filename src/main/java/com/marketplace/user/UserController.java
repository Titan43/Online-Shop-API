package com.marketplace.user;
import com.marketplace.security.JwtUtil;
import com.marketplace.user.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    private final IUserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(IUserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public User getUser(@RequestParam String username){
        return userService.getUser(username);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED, reason = "User was successfully created(CODE 201)")
    public void registerNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK, reason = "User was successfully deleted(CODE 200)")
    public void deleteUser(@RequestParam String username){
        userService.deleteUser(username);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK, reason = "User was successfully updated(CODE 200)")
    public void updateUser(@RequestParam String username, @RequestBody User user){
        userService.updateUser(username, user);
    }

    @PostMapping(path = "/login")
    public void login(){
    }
    @GetMapping(path = "/logout")
    public void logout(){
    }

}
