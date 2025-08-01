package com.uvg.earth.way.service;

import java.util.List;

import com.uvg.earth.way.model.Role;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.RoleRepository;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.interfaces.IRoleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void removeUserRole(Long userId, String roleNameToRemove) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Role roleToRemove = roleRepository.findByName(roleNameToRemove)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        List<Role> currentRoles = user.getRoles();

        if (currentRoles.contains(roleToRemove)) {
            currentRoles.remove(roleToRemove);
            user.setRoles(currentRoles);
            userRepository.save(user);
        } else {
            throw new RuntimeException("El usuario no tiene asignado el rol: " + roleNameToRemove);
        }
    }

    @Override
    public void changeUserRole(Long userId, String newRoleName){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Role newRole = roleRepository.findByName(newRoleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        List<Role> currentRoles = user.getRoles();
        if (!currentRoles.contains(newRole)) {
            currentRoles.add(newRole);
            user.setRoles(currentRoles);
        }
        userRepository.save(user);
    }
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