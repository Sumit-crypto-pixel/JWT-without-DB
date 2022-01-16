package com.sumit.jwt.controller;

import com.sumit.jwt.model.JWTRequest;
import com.sumit.jwt.model.JWTResponse;
import com.sumit.jwt.service.UserService;
import com.sumit.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
   @Autowired
    private JWTUtility jwtUtility;
@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private UserService userService;

    @GetMapping("/")
    public String home(){
        return "Welcome Sumit !";
    }
    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new Exception("Invalid credentials", e);
        }
        final UserDetails userDetails
                =userService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtUtility.generateToken(userDetails);
  // once token is created then wrap it into JWT response and send it back.
        return new JWTResponse(token);

    }
}
