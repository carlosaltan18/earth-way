package com.uvg.earth.way.service;

import java.util.List;

import com.uvg.earth.way.model.Role;
import com.uvg.earth.way.repository.RoleRepository;
import com.uvg.earth.way.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService implements IRoleService {

    @Autowired
    RoleRepository roleRepository;


    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {
        roleRepository.delete(role);
    }

}