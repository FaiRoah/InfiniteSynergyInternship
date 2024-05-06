package com.fairoah.bankServer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private BigDecimal balance;

    private List<Role> roles = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
}
