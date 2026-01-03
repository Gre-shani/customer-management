package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CustomerResponseDTO {

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private String nic;

    private List<CustomerMobileDTO> mobiles;
    private List<CustomerAddressDTO> addresses;
    private List<CustomerFamilyDTO> families;

    // Full constructor
    public CustomerResponseDTO(Long id, String name, LocalDate dateOfBirth, String nic,
                               List<CustomerMobileDTO> mobiles,
                               List<CustomerAddressDTO> addresses,
                               List<CustomerFamilyDTO> families) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nic = nic;
        this.mobiles = mobiles;
        this.addresses = addresses;
        this.families = families;
    }

    // Constructor with only basic fields
    public CustomerResponseDTO(Long id, String name, LocalDate dateOfBirth, String nic) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nic = nic;
    }
}
