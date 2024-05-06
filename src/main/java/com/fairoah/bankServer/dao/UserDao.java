package com.fairoah.bankServer.dao;

import com.fairoah.bankServer.model.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserDao {
    void addUser(User user);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    BigDecimal getBalanceByUsername(String username);

    void sendMoney(String usernameFrom, String usernameTo, BigDecimal amount);
}
