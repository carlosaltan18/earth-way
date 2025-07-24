package com.uvg.earth.way.controller;


import java.util.Map;
import java.util.HashMap;

import com.uvg.earth.way.dto.AuthResponseDto;
import com.uvg.earth.way.dto.LoginUserDto;
import com.uvg.earth.way.dto.RegisterUserDto;
import com.uvg.earth.way.model.User;
import com.uvg.earth.way.security.JwtConfig;
import com.uvg.earth.way.service.interfaces.IAuthServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController{

    private final JwtConfig jwtConfig;
    private final IAuthServices authService;
    private static final String MESSAGE = "message";

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterUserDto registerUserDto){

        Map<String, String> response = new HashMap<>();

        try {
            User registerdUser = authService.register(registerUserDto);
            response.put(MESSAGE, "Register successful" + registerdUser);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put(MESSAGE, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put(MESSAGE, "Error");
            response.put("err", "An error occurred while adding the user " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginUserDto loginUserDto){

        Map<String, Object> response = new HashMap<>();

        try {
            User authenticatedUser = authService.login(loginUserDto);
            String jwtToken = jwtConfig.buildToken(authenticatedUser.getRoles() ,authenticatedUser);
            AuthResponseDto authResponseDto = new AuthResponseDto();
            authResponseDto.setToken(jwtToken);
            authResponseDto.setExpiresIn(jwtConfig.getJwtExpiration());

            response.put(MESSAGE, "Logueado con éxito");
            response.put("payload", authResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error",  "Error al iniciar sesión");
            response.put(MESSAGE,  e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


}