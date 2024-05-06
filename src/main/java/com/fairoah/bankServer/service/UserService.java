package com.fairoah.bankServer.service;

import com.fairoah.bankServer.dao.UserDao;
import com.fairoah.bankServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user){
        userDao.addUser(user);
    }


    public BigDecimal getBalance(String username){
        return userDao.getBalanceByUsername(username);
    }

    public void sendMoney (String usernameFrom, String usernameTo, BigDecimal amount) {
        userDao.sendMoney(usernameFrom, usernameTo, amount);
    }
}
