package com.uvg.earth.way.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> errorResponse = new HashMap<>();

        // Diferenciar el tipo de error si es posible
        if (authException instanceof BadCredentialsException) {
            errorResponse.put("error", "Credenciales inválidas.");
        } else if (authException instanceof CredentialsExpiredException) {
            errorResponse.put("error", "Las credenciales han expirado.");
        } else {
            // Mensaje genérico para otros errores de autenticación
            errorResponse.put("error", "No estás autorizado. Token inválido o ausente.");
        }

        // (Opcional) Puedes agregar detalles del error en desarrollo:
        errorResponse.put("message", authException.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
