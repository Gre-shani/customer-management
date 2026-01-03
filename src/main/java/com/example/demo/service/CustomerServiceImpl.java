package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;

import javax.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CityRepository cityRepository,
                               CountryRepository countryRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setNic(dto.getNic());

        // Mobiles
        if (dto.getMobiles() != null) {
            List<CustomerMobile> mobiles = dto.getMobiles().stream()
                    .map(m -> {
                        CustomerMobile mobile = new CustomerMobile();
                        mobile.setMobileNumber(m.getMobileNumber());
                        mobile.setCustomer(customer);
                        return mobile;
                    }).collect(Collectors.toList());
            customer.setMobiles(mobiles);
        }

        // Addresses
        if (dto.getAddresses() != null) {
            List<CustomerAddress> addresses = dto.getAddresses().stream()
                    .map(a -> {
                        CustomerAddress addr = new CustomerAddress();
                        addr.setAddressLine1(a.getAddressLine1());
                        addr.setAddressLine2(a.getAddressLine2());
                        addr.setCustomer(customer);

                        if (a.getCityId() != null) {
                            addr.setCity(cityRepository.getReferenceById(a.getCityId()));
                        }
                        if (a.getCountryId() != null) {
                            addr.setCountry(countryRepository.getReferenceById(a.getCountryId()));
                        }

                        return addr;
                    }).collect(Collectors.toList());
            customer.setAddresses(addresses);
        }

        // Families
        if (dto.getFamilies() != null) {
            List<CustomerFamily> families = dto.getFamilies().stream()
                    .map(f -> {
                        CustomerFamily family = new CustomerFamily();
                        family.setCustomer(customer);

                        if (f.getFamilyMemberId() != null) {
                            Customer member = customerRepository.getReferenceById(f.getFamilyMemberId());
                            family.setFamilyMember(member);
                        }

                        family.setRelationship(f.getRelationship());
                        return family;
                    }).collect(Collectors.toList());
            customer.setFamilies(families);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(savedCustomer);
    }

    @Override
    public Page<CustomerResponseDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return mapToResponseDTO(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        customer.setName(dto.getName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setNic(dto.getNic());

        // Mobiles
        customer.getMobiles().clear();
        if (dto.getMobiles() != null) {
            List<CustomerMobile> mobiles = dto.getMobiles().stream()
                    .map(m -> {
                        CustomerMobile mobile = new CustomerMobile();
                        mobile.setMobileNumber(m.getMobileNumber());
                        mobile.setCustomer(customer);
                        return mobile;
                    }).collect(Collectors.toList());
            customer.setMobiles(mobiles);
        }

        // Addresses
        customer.getAddresses().clear();
        if (dto.getAddresses() != null) {
            List<CustomerAddress> addresses = dto.getAddresses().stream()
                    .map(a -> {
                        CustomerAddress addr = new CustomerAddress();
                        addr.setAddressLine1(a.getAddressLine1());
                        addr.setAddressLine2(a.getAddressLine2());
                        addr.setCustomer(customer);

                        if (a.getCityId() != null) {
                            addr.setCity(cityRepository.getReferenceById(a.getCityId()));
                        }
                        if (a.getCountryId() != null) {
                            addr.setCountry(countryRepository.getReferenceById(a.getCountryId()));
                        }

                        return addr;
                    }).collect(Collectors.toList());
            customer.setAddresses(addresses);
        }

        // Families
        customer.getFamilies().clear();
        if (dto.getFamilies() != null) {
            List<CustomerFamily> families = dto.getFamilies().stream()
                    .map(f -> {
                        CustomerFamily family = new CustomerFamily();
                        family.setCustomer(customer);

                        if (f.getFamilyMemberId() != null) {
                            Customer member = customerRepository.getReferenceById(f.getFamilyMemberId());
                            family.setFamilyMember(member);
                        }

                        family.setRelationship(f.getRelationship());
                        return family;
                    }).collect(Collectors.toList());
            customer.setFamilies(families);
        }

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponseDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Map entity → DTO
    private CustomerResponseDTO mapToResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getDateOfBirth(),
                customer.getNic()
        );

        if (customer.getMobiles() != null) {
            dto.setMobiles(customer.getMobiles().stream()
                    .map(m -> new CustomerMobileDTO(m.getId(), m.getMobileNumber()))
                    .collect(Collectors.toList()));
        }

        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream()
                    .map(a -> new CustomerAddressDTO(
                            a.getId(),
                            a.getAddressLine1(),
                            a.getAddressLine2(),
                            a.getCity() != null ? a.getCity().getId() : null,
                            a.getCountry() != null ? a.getCountry().getId() : null
                    ))
                    .collect(Collectors.toList()));
        }

        if (customer.getFamilies() != null) {
            dto.setFamilies(customer.getFamilies().stream()
                    .map(f -> new CustomerFamilyDTO(
                            f.getFamilyMember().getId(),
                            f.getRelationship()
                    ))
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
