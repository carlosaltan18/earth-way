package com.uvg.earth.way.controller;

import com.uvg.earth.way.model.Role;
import com.uvg.earth.way.service.interfaces.IRoleService;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequestMapping(value = "/api/v1/role")
@RestController
public class RoleController {
    private final IRoleService roleService;
    private static final String MESSAGE = "message";
    private static final String ERROR = "Ocurrió un error";
    private static final String ADMIN = "ADMIN";

    @RolesAllowed(ADMIN)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRoles() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Role> roles = roleService.getAllRoles();
            response.put("roles", roles);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed(ADMIN)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRoleById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Role role = roleService.findRoleById(id);
            if (role != null) {
                response.put("role", role);
                return ResponseEntity.ok(response);
            } else {
                response.put(MESSAGE, "Rol no encontrado");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed(ADMIN)
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveRole(@RequestBody Role role) {
        Map<String, Object> response = new HashMap<>();
        try {
            roleService.saveRole(role);
            response.put(MESSAGE, "Rol guardado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed(ADMIN)
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteRole(@RequestBody Role role) {
        Map<String, Object> response = new HashMap<>();
        try {
            roleService.deleteRole(role);
            response.put(MESSAGE, "Rol eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed(ADMIN)
    @PostMapping("/user/{userId}/add")
    public ResponseEntity<Map<String, Object>> changeUserRole(
            @PathVariable Long userId,
            @RequestParam String roleName) {

        Map<String, Object> response = new HashMap<>();
        try {
            roleService.changeUserRole(userId, roleName);
            response.put(MESSAGE, "Rol agregado al usuario");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed(ADMIN)
    @PostMapping("/user/{userId}/remove")
    public ResponseEntity<Map<String, Object>> removeUserRole(
            @PathVariable Long userId,
            @RequestParam String roleName) {

        Map<String, Object> response = new HashMap<>();
        try {
            roleService.removeUserRole(userId, roleName);
            response.put(MESSAGE, "Rol removido del usuario");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}