package com.example.rental.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.rental.entity.ZipFile;
import com.example.rental.service.ZipFileService;

@RestController
@RequestMapping("/zip/files")
public class ZipFileController {

    @Autowired
    private ZipFileService zipFileService;

    // Upload ZIP
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file) {

        try {
            ZipFile savedFile = zipFileService.uploadFile(file);

            return ResponseEntity.ok(
                    "File uploaded successfully. ID: " + savedFile.getId()
            );

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to upload file");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Get all files
    @GetMapping
    public ResponseEntity<List<ZipFile>> getAllFiles() {
        return ResponseEntity.ok(zipFileService.getAllFiles());
    }

    // Delete all files
    @DeleteMapping
    public ResponseEntity<String> deleteAllFiles() {

        zipFileService.deleteAllFiles();

        return ResponseEntity.ok("All files deleted successfully");
    }
}