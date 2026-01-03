package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.CustomerBulkService;

@RestController
@RequestMapping("/api/customers/bulk")
public class CustomerBulkController {

    private final CustomerBulkService bulkService;

    public CustomerBulkController(CustomerBulkService bulkService) {
        this.bulkService = bulkService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a valid Excel file");
        }

        try {
            bulkService.processExcel(file);
            return ResponseEntity.ok().body("Excel file processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to process Excel file: " + e.getMessage());
        }
    }
}
