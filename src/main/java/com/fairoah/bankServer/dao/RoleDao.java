package com.fairoah.bankServer.dao;

import com.fairoah.bankServer.model.Role;

public interface RoleDao {
    public Role findByName(String name);
}
