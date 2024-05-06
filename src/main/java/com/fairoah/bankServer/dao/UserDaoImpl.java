package com.fairoah.bankServer.dao;

import com.fairoah.bankServer.exception.InsufficientFundsException;
import com.fairoah.bankServer.exception.UserAlreadyExistsException;
import com.fairoah.bankServer.exception.UserNotFoundException;
import com.fairoah.bankServer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final String usersTableName = "users";
    private final String userRolesTableName = "user_roles";

    private final int defaultRole = 1;  // "USER"
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {

        String countQery = "SELECT COUNT(*) FROM " + usersTableName + " WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(countQery, new Object[]{user.getUsername()}, Integer.class);

        if (count != null && count > 0) {
            throw new UserAlreadyExistsException("A user with the login " + user.getUsername() + " already exists.");
        }

        String query = String.format("""
                WITH inserted_user AS (
                    INSERT INTO %s (username, password)
                    VALUES (?, ?)
                    RETURNING id
                )
                INSERT INTO %s (user_id, role_id)
                SELECT id, %d FROM inserted_user;
                """, usersTableName, userRolesTableName, defaultRole);
        jdbcTemplate.update(query, user.getUsername(), user.getPassword());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String query = "SELECT * FROM " + usersTableName + " WHERE username=?";  //todo дописать получение ролей
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                new Object[]{username},
                (rs, rowNum) ->
                        new User(
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getBigDecimal("balance")
                        )));
    }

    @Override
    public Boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM " + usersTableName + " WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{username}, Integer.class);

        return count != null && count > 0;
    }

    @Override
    public BigDecimal getBalanceByUsername(String username) {
        String query = "SELECT balance FROM " + usersTableName + " WHERE username = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{username}, BigDecimal.class);
    }

    @Override
    public void sendMoney(String usernameFrom, String usernameTo, BigDecimal amount) {
        if (getBalanceByUsername(usernameFrom).compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough balance to make the transaction");
        }
        if (!existsByUsername(usernameFrom)) {
            throw new UserNotFoundException("Sender username not found");
        }
        if (!existsByUsername(usernameTo)) {
            throw new UserNotFoundException("Recipient username not found");
        }
        String querySend = "UPDATE " + usersTableName + " SET balance = balance - ? WHERE username = ?;";
        String queryReceive = "UPDATE " + usersTableName + " SET balance = balance + ? WHERE username = ?";
        jdbcTemplate.update(querySend, amount, usernameFrom);
        jdbcTemplate.update(queryReceive, amount, usernameTo);  // todo сделать транзакционно
    }
}


