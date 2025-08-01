package com.uvg.earth.way.service.interfaces;

import com.uvg.earth.way.model.Role;

import java.util.List;


public interface IRoleService {

    void changeUserRole(Long userId, String newRoleName);
    Role findRoleById(Long id);

    List<Role> getAllRoles();

    void saveRole(Role role);

    void deleteRole(Role role);
    void removeUserRole(Long userId, String roleNameToRemove);
}