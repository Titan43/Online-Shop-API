package com.marketplace.controllers;

import com.marketplace.security.AuthRequest;
import com.marketplace.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest request){
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(request.getUsername());
        }
        catch (UsernameNotFoundException e){
            return new ResponseEntity<>("Username does not exist(CODE 401)", HttpStatus.UNAUTHORIZED);
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (BadCredentialsException e){
            return new ResponseEntity<>("Wrong password(CODE 401)", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(jwtUtil.generateToken(user), HttpStatus.OK);
    }
}