package com.uvg.earth.way.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uvg.earth.way.dto.ChangePasswordDto;
import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.dto.UserOrgDTO;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.repository.UserRepository;
import com.uvg.earth.way.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping(value = "/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final static String MESSAGE = "message";
    private final static String ERROR = "error";
    private final static String ADMIN = "ADMIN";
    private final static String USER = "USER";
    private final static String ORGANIZATION = "ORGANIZATION";
    private final UserRepository userRepository;

    @RolesAllowed({ADMIN})
    @GetMapping("/get-user")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(required = false) String email) {
        Map<String, Object> response = new HashMap<>();
        try {

            Page<User> userPage = userService.getAllUsers(page, size, email);
            response.put("payload", userPage.getContent());
            response.put(MESSAGE, "Users retrieved successfully");

            response.put("users", userPage.getContent());
            response.put("totalPages", userPage.getTotalPages());
            response.put("currentPage", userPage.getNumber());
            response.put("totalElements", userPage.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "An error get users " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN})
    @GetMapping("/get-user/{idUser}")
    public ResponseEntity<Map<String, Object>> findUsers(@PathVariable Long idUser) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> user = userService.findById(idUser);
            if (user.isPresent()) {
                response.put(MESSAGE, user);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "An error finding user " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUsers() {
        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();
        try {
            userService.deleteUser(customUserDetails.getId());
            response.put(MESSAGE, "User deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, ERROR);
            response.put("err", "An error ocurred deliting user " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }


    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody ChangePasswordDto password) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User customUserDetails = (User) authentication.getPrincipal();

            userService.changePassword(
                    customUserDetails.getId(),
                    password.getNewPassword(),
                    password.getConfirmPassword()
            );

            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", "Passwords do not match or invalid input");
            return ResponseEntity.badRequest().body(response);

        } catch (UsernameNotFoundException e) {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "An unexpected error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @RolesAllowed({ADMIN})
    @PutMapping("/update/{idUser}")
    public ResponseEntity<Map<String, Object>> updateUserId(@PathVariable Long idUser, @RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        try{
            userService.updateUser(userDto, idUser);
            response.put(MESSAGE, "User Updated Successfully");
            return ResponseEntity.ok(response);

        }catch(EntityNotFoundException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RolesAllowed({ADMIN, USER, ORGANIZATION})
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser( @RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();
        try{
            userService.updateUser(userDto, customUserDetails.getId());
            response.put(MESSAGE, "User Updated Successfully");
            return ResponseEntity.ok(response);

        }catch(EntityNotFoundException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RolesAllowed({ADMIN, ORGANIZATION})
    @GetMapping("/userorganization")
    public ResponseEntity<?> getOrganizations() {
        // Buscar usuarios con rol ORGANIZATION
        List<User> organizations = userRepository.findByRoleName("ROLE_ORGANIZATION");

        if (organizations == null || organizations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", HttpStatus.NOT_FOUND.value(),
                            "message", "No se encontraron usuarios con el rol ORGANIZATION",
                            "data", List.of()
                    ));
        }

        // Convertir entidades a DTOs
        List<UserOrgDTO> response = organizations.stream()
                .map(UserOrgDTO::fromEntity)
                .toList();

        // Validar datos dentro de cada usuario (por ejemplo, email y nombre)
        boolean hasInvalid = response.stream().anyMatch(u ->
                u.getEmail() == null || u.getEmail().isBlank() ||
                        u.getName() == null || u.getName().isBlank()
        );

        if (hasInvalid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", HttpStatus.BAD_REQUEST.value(),
                            "message", "Algunos usuarios no tienen datos válidos registrados",
                            "data", response
                    ));
        }

        // Respuesta exitosa
        return ResponseEntity.ok(Map.of(
                "status", HttpStatus.OK.value(),
                "message", "Usuarios con rol ORGANIZATION obtenidos correctamente",
                "data", response
        ));
    }


}
