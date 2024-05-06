package com.fairoah.bankServer.controller;

import com.fairoah.bankServer.dto.SendMoneyDto;
import com.fairoah.bankServer.security.JwtProvider;
import com.fairoah.bankServer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class MoneyController {

    private JwtProvider tokenProvider;
    private final UserService userService;

    @Autowired
    public MoneyController(JwtProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @GetMapping("/money")
    public ResponseEntity<?> balance(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        String username = tokenProvider.getUsernameFromJwt(token);
        log.info("User " + username + " balance is " + userService.getBalance(username));
        return ResponseEntity.status(HttpStatus.OK).body(userService.getBalance(username));
    }

    @PostMapping("/money")
    public ResponseEntity<?> sendMoney(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody SendMoneyDto sendMoneyDto
    ) {
        String token = bearerToken.substring(7);
        String username = tokenProvider.getUsernameFromJwt(token);

        userService.sendMoney(username,sendMoneyDto.getTo(),sendMoneyDto.getAmount());

        log.info("User " + username + " sent " + sendMoneyDto.getAmount() + " to the user " + sendMoneyDto.getTo());
        return ResponseEntity.ok("Transaction completed");
    }
}
