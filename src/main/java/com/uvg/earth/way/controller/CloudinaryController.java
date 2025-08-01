package com.uvg.earth.way.controller;

import com.uvg.earth.way.dto.ImageUploadResponse;
import com.uvg.earth.way.service.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@AllArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            ImageUploadResponse response = new ImageUploadResponse(imageUrl, "Imagen subida correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ImageUploadResponse(null, "Error al subir imagen: " + e.getMessage())
            );
        }
    }
}