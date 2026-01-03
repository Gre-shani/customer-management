package com.example.demo.repository;

import com.example.demo.entity.CustomerFamily;
import com.example.demo.entity.CustomerFamilyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerFamilyRepository
        extends JpaRepository<CustomerFamily, CustomerFamilyId> {
}
