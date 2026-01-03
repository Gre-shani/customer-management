package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface CustomerBulkService {
    void processExcel(MultipartFile file);
}
