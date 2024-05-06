package com.fairoah.bankServer.dao;

import com.fairoah.bankServer.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl implements RoleDao{

    private final String tableName = "roles";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Role findByName(String name) {
        String query = "SELECT * FROM " + tableName + " WHERE role_name=?";
        return jdbcTemplate.queryForObject(
                query,
                new Object[]{name},
                (rs, rowNum) ->
                        new Role(
                                rs.getLong("id"),
                                rs.getString("role_name")
                        ));
    }
}
