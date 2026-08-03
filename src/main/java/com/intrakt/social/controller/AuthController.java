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

    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomeUserDetailsService customUserDetails;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(BCryptPasswordEncoder passwordEncoder,  CustomeUserDetailsService customUserDetails, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
        this.userRepository = userRepository;
    }

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
        return new AuthResponse(token,"register success", savedUser.getRole());
    }

    @PostMapping("/signin")
    public AuthResponse signin(@RequestBody LoginRequest loginRequest) throws Exception {
        Authentication authentication = authenticate(loginRequest);
        String token= JwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new Exception("User not found");
        }

        return new AuthResponse(token,"login success", user.getRole());
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
