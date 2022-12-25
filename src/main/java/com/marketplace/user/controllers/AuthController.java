package com.marketplace.user.controllers;

import com.marketplace.security.AuthRequest;
import com.marketplace.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path="api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        if(user != null){
            return jwtUtil.generateToken(user);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong (CODE 400)");
    }

}
