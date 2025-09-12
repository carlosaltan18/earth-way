package com.uvg.earth.way.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Método HTTP no soportado");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Recurso no encontrado");
        response.put("message", "La ruta solicitada no existe: " + ex.getRequestURL());
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error interno del servidor");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Error de negocio");
        error.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}

