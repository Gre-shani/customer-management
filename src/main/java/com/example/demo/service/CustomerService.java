package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;  

import com.example.demo.dto.CustomerRequestDTO;
import com.example.demo.dto.CustomerResponseDTO;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);

    // Get all customers with pagination and sorting
    Page<CustomerResponseDTO> getAllCustomers(Pageable pageable);

    // Get customer by ID
    CustomerResponseDTO getCustomerById(Long id);

    // Update customer
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO);

    // Delete customer
    void deleteCustomer(Long id);
}