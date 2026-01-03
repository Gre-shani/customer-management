package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddressDTO {
    private Long id;
    private String addressLine1;
    private String addressLine2;
    private Long cityId;       // FK reference
    private Long countryId;    // FK reference
}
