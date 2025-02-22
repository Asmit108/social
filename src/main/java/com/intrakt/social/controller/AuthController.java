package com.intrakt.social.controller;

import com.intrakt.social.config.JwtProvider;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.LoginRequest;
import com.intrakt.social.response.AuthResponse;
import com.intrakt.social.service.CustomeUserDetailsService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomeUserDetailsService customUserDetails;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) throws Exception {
        User isExist=userRepository.findByEmail(user.getEmail());
        if(isExist!=null){
            throw new Exception("Email already used with another account");
        }

        User newUser=new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole("USER");
        User savedUser=userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser, savedUser);
        String token= JwtProvider.generateToken(authentication);
        AuthResponse res=new AuthResponse(token,"register success", savedUser.getRole());
        return res;
    }

    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticate(loginRequest);
        String token= JwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new Exception("User not found");
        }

        AuthResponse res=new AuthResponse(token,"login success", user.getRole());
        return res;
    }

    private Authentication authenticate(LoginRequest loginRequest) throws Exception {

        UserDetails userDetails = customUserDetails.loadUserByUsername(loginRequest.getEmail());
        if(userDetails==null){
            throw new BadCredentialsException("invalid username...");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())){
            throw new BadCredentialsException("wrong password...");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
