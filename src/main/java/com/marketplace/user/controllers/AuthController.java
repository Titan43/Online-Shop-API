package com.marketplace.user.controllers;

import com.marketplace.security.AuthRequest;
import com.marketplace.security.JwtUtil;
import lombok.AllArgsConstructor;
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

import static com.marketplace.constants.IAPIConstants.API_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping(path=API_PREFIX+"auth")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final UserDetailsService userDetailsService;
    @Autowired
    private final JwtUtil jwtUtil;

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
