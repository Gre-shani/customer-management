package com.example.demo.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerAddress;
import com.example.demo.entity.CustomerMobile;
import com.example.demo.repository.CustomerRepository;

@Service
public class CustomerBulkServiceImpl implements CustomerBulkService {

    private static final Logger log = LoggerFactory.getLogger(CustomerBulkServiceImpl.class);
    private static final int BATCH_SIZE = 500;

    private final CustomerRepository customerRepository;

    public CustomerBulkServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void processExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(is), 100)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Customer> batch = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header

                try {
                    String name = getCellString(row.getCell(0));
                    LocalDate dob = getCellDate(row.getCell(1));
                    String nic = getCellString(row.getCell(2));

                    if (name == null || dob == null || nic == null) {
                        log.warn("Skipping row {} due to missing mandatory fields", row.getRowNum());
                        continue;
                    }

                    Customer customer = new Customer();
                    customer.setName(name);
                    customer.setDateOfBirth(dob);
                    customer.setNic(nic);

                    // Optional: handle addresses if included
                    if (row.getCell(3) != null || row.getCell(4) != null) {
                        List<CustomerAddress> addresses = new ArrayList<>();
                        String address1 = getCellString(row.getCell(3));
                        String address2 = getCellString(row.getCell(4));

                        if (address1 != null) {
                            CustomerAddress addr = new CustomerAddress();
                            addr.setAddressLine1(address1);
                            addr.setAddressLine2(address2);
                            addr.setCustomer(customer); // link back to customer
                            addresses.add(addr);
                        }
                        customer.setAddresses(addresses);
                    }

                    // Optional: handle mobiles (column 5)
                    if (row.getCell(5) != null) {
                        List<CustomerMobile> mobiles = new ArrayList<>();
                        String mobile = getCellString(row.getCell(5));
                        if (mobile != null) {
                            CustomerMobile m = new CustomerMobile();
                            m.setMobileNumber(mobile);
                            m.setCustomer(customer);
                            mobiles.add(m);
                        }
                        customer.setMobiles(mobiles);
                    }

                    batch.add(customer);

                    if (batch.size() >= BATCH_SIZE) {
                        customerRepository.saveAll(batch);
                        batch.clear();
                    }

                } catch (Exception e) {
                    log.error("Failed to process row {}: {}", row.getRowNum(), e.getMessage());
                }
            }

            if (!batch.isEmpty()) {
                customerRepository.saveAll(batch);
            }

            log.info("Excel file processed successfully");

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file", e);
        }
    }

    private String getCellString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return null;
    }

    private LocalDate getCellDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        return null;
    }
}
