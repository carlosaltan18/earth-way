package com.uvg.earth.way.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.uvg.earth.way.dto.ChangePasswordDto;
import com.uvg.earth.way.dto.UserDto;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping(value = "/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final static String MESSAGE = "message";
    private final static String ERROR = "error";

    @RolesAllowed({"ADMIN"})
    @GetMapping(value = "/")
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

    @RolesAllowed("ADMIN")
    @GetMapping("/{idUser}")
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

    @RolesAllowed({"ADMIN", "USER", "ORGANIZATION"})
    @DeleteMapping("/")
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


    @RolesAllowed({"ADMIN", "USER", "ORGANIZATION"})
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUserDetails = (User) authentication.getPrincipal();
        userService.changePassword(customUserDetails.getId(),password.getNewPassword(), password.getConfirmPassword());
        return ResponseEntity.ok("Password changed");
    }

    @RolesAllowed({"ADMIN", "USER", "ORGANIZATION"})
    @PutMapping("/{idUser}")
    public ResponseEntity<Map<String, String>> updateUserId(@PathVariable Long idUser, @RequestBody UserDto userDto) {
        Map<String, String> response = new HashMap<>();
        try{
            userService.updateUser(userDto, idUser);
            response.put(MESSAGE, "User Updated Successfully");
            return ResponseEntity.ok(response);

        }catch(EntityNotFoundException e){
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RolesAllowed("ADMIN")
    @PutMapping("")
    public ResponseEntity<Map<String, String>> updateUser( @RequestBody UserDto userDto) {
        Map<String, String> response = new HashMap<>();
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
}