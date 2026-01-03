package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "NIC is mandatory")
    @Pattern(
        regexp = "^[0-9]{9}[VvXx]$|^[0-9]{12}$",
        message = "Invalid NIC format"
    )
    private String nic;

    @Valid
    private List<CustomerMobileDTO> mobiles;

    @Valid
    private List<CustomerAddressDTO> addresses;

    @Valid
    private List<CustomerFamilyDTO> families;
}
