package com.fairoah.bankServer.controller;

import com.fairoah.bankServer.dto.AuthResponseDto;
import com.fairoah.bankServer.dto.UserLoginDto;
import com.fairoah.bankServer.dto.UserRegisterDto;
import com.fairoah.bankServer.model.Role;
import com.fairoah.bankServer.model.User;
import com.fairoah.bankServer.security.JwtProvider;
import com.fairoah.bankServer.security.constants.UserRoles;
import com.fairoah.bankServer.service.RoleService;
import com.fairoah.bankServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRegisterDto userRegisterDto) {

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        Role roles = roleService.findByName(UserRoles.USER.name());
        user.setRoles(Collections.singletonList(roles));

        userService.addUser(user);
        log.info("User " + userRegisterDto.getUsername() + " signed up successfully");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        log.info("User " + userLoginDto.getUsername() + " signed in successfully");
        return ResponseEntity.ok().body(new AuthResponseDto(token));
    }
}
