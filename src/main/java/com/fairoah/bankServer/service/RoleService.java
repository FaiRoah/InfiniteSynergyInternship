package com.fairoah.bankServer.service;

import com.fairoah.bankServer.dao.RoleDao;
import com.fairoah.bankServer.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role findByName(String name){
        return roleDao.findByName(name);
    }


}
