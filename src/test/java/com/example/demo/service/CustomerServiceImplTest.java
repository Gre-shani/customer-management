package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import com.example.demo.dto.CustomerAddressDTO;
import com.example.demo.dto.CustomerFamilyDTO;
import com.example.demo.dto.CustomerMobileDTO;
import com.example.demo.dto.CustomerRequestDTO;
import com.example.demo.entity.City;
import com.example.demo.entity.Country;
import com.example.demo.entity.Customer;
import com.example.demo.dto.CustomerResponseDTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.repository.CityRepository;
import com.example.demo.repository.CountryRepository;
import com.example.demo.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CityRepository cityRepository;

    @Mock
    CountryRepository countryRepository;

    @InjectMocks
    CustomerServiceImpl service;

    @Test
    void createCustomer_shouldSaveAndReturnResponse() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setName("Alice");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setNic("123456789V");

        // mobiles
        dto.setMobiles(Arrays.asList(new CustomerMobileDTO(null, "0711234567")));

        // addresses with city/country references
        dto.setAddresses(Arrays.asList(new CustomerAddressDTO(null, "Addr1", "Addr2", 1L, 2L)));

        // family refererence
        dto.setFamilies(Arrays.asList(new CustomerFamilyDTO(5L, "Spouse")));

        when(cityRepository.getReferenceById(1L)).thenReturn(new City(1L, "CityX", null));
        when(countryRepository.getReferenceById(2L)).thenReturn(new Country(2L, "CountryY"));
        when(customerRepository.getReferenceById(5L)).thenReturn(new Customer(5L, "Bob", null, "987654321V", null, null, null));

        when(customerRepository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            c.setId(10L);
            return c;
        });

        CustomerResponseDTO resp = service.createCustomer(dto);

        // one assert per behavior: verify save was called
        verify(customerRepository, times(1)).save(any(Customer.class));
        assertEquals("Alice", resp.getName());
    }

    @Test
    void getCustomerById_found() {
        Customer c = new Customer(2L, "Bob", LocalDate.of(1980,2,2), "111111111V", null, null, null);
        when(customerRepository.findById(2L)).thenReturn(Optional.of(c));

        CustomerResponseDTO resp = service.getCustomerById(2L);

        verify(customerRepository, times(1)).findById(2L);
        assertEquals(2L, resp.getId());
    }

    @Test
    void getCustomerById_notFound_throws() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getCustomerById(99L));
        assertTrue(ex.getMessage().contains("Customer not found"));
    }

    @Test
    void updateCustomer_shouldSaveUpdated() {
        Customer existing = new Customer();
        existing.setId(3L);
        existing.setName("Carol");
        existing.setDateOfBirth(LocalDate.of(1975,3,3));
        existing.setNic("222222222V");
        existing.setMobiles(new java.util.ArrayList<>());
        existing.setAddresses(new java.util.ArrayList<>());
        existing.setFamilies(new java.util.ArrayList<>());
        when(customerRepository.findById(3L)).thenReturn(Optional.of(existing));
        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> i.getArgument(0));

        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setName("Carolyn");
        dto.setDateOfBirth(LocalDate.of(1975,3,3));
        dto.setNic("222222222V");

        CustomerResponseDTO resp = service.updateCustomer(3L, dto);

        verify(customerRepository, times(1)).findById(3L);
        verify(customerRepository, times(1)).save(any(Customer.class));
        assertEquals("Carolyn", resp.getName());
    }

    @Test
    void deleteCustomer_shouldDeleteWhenExists() {
        when(customerRepository.existsById(4L)).thenReturn(true);
        service.deleteCustomer(4L);
        verify(customerRepository, times(1)).deleteById(4L);
    }

    @Test
    void deleteCustomer_notFound_throws() {
        when(customerRepository.existsById(99L)).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.deleteCustomer(99L));
        assertTrue(ex.getMessage().contains("Customer not found"));
    }
}
