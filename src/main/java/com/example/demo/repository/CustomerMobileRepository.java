package com.example.demo.repository;

import com.example.demo.entity.CustomerMobile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerMobileRepository extends JpaRepository<CustomerMobile, Long> {
}
