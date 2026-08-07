package com.intrakt.social.controller;

import com.intrakt.social.config.JwtProvider;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.UserRepository;
import com.intrakt.social.request.LoginRequest;
import com.intrakt.social.request.UserRequest;
import com.intrakt.social.response.AuthResponse;
import com.intrakt.social.service.CustomeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomeUserDetailsService customUserDetails;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(BCryptPasswordEncoder passwordEncoder,  CustomeUserDetailsService customUserDetails, UserRepository userRepository, JwtProvider jwtProvider) {
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody UserRequest registerRequest) {
        User isExist=userRepository.findByEmail(registerRequest.getEmail());
        if(isExist!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists with this email");
        }

        User newUser=new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setGender(registerRequest.getGender());
        // Parse and set role (normalize to lowercase)
        User.Role role = registerRequest.getRole();
        if (User.Role.ADMIN == role) {
            newUser.setRole(User.Role.ADMIN);
        } else {
            newUser.setRole(User.Role.USER);
        }
        User savedUser=userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser, savedUser);
        String token= jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token,"register success", String.valueOf(savedUser.getRole()));
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest);
        String token= jwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        AuthResponse authResponse = new AuthResponse(token,"login success", String.valueOf(user.getRole()));
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    private Authentication authenticate(LoginRequest loginRequest) {

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
