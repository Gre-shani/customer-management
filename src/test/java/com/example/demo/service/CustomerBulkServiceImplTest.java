package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerBulkServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerBulkServiceImpl service;

    @Test
    void processExcel_shouldSaveBatch() throws Exception {
        // create a small XLSX in memory with header + 2 rows
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("sheet1");
        Row h = sh.createRow(0);
        h.createCell(0).setCellValue("name");
        h.createCell(1).setCellValue("dob");
        h.createCell(2).setCellValue("nic");

        // create many rows to trigger in-loop batch save (BATCH_SIZE = 500)
        int rows = 510; // ensure at least one batch flush
        // prepare a date cell style so POI writes a proper date (numeric) cell
        org.apache.poi.ss.usermodel.CreationHelper createHelper = wb.getCreationHelper();
        org.apache.poi.ss.usermodel.CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

        for (int i = 1; i <= rows; i++) {
            Row r = sh.createRow(i);
            r.createCell(0).setCellValue("Name" + i);
            org.apache.poi.ss.usermodel.Cell dcell = r.createCell(1);
            dcell.setCellValue(java.sql.Date.valueOf(LocalDate.of(1990,1,1)));
            dcell.setCellStyle(dateStyle);
            r.createCell(2).setCellValue("NIC" + i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        wb.close();

        byte[] bytes = bos.toByteArray();

        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(bytes));

        // capture saveAll calls and assert that at least one batch was saved
        doAnswer(inv -> {
            List<Customer> list = inv.getArgument(0);
            assertTrue(list.size() >= 1);
            return list;
        }).when(customerRepository).saveAll(anyList());

        service.processExcel(file);

        verify(customerRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void processExcel_emptyFile_shouldNotSave() throws Exception {
        // create workbook with only header
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet("s");
        Row h = sh.createRow(0);
        h.createCell(0).setCellValue("name");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        wb.close();

        byte[] bytes = bos.toByteArray();

        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(bytes));

        service.processExcel(file);

        verify(customerRepository, never()).saveAll(anyList());
    }

    @Test
    void processExcel_ioError_shouldThrowRuntime() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenThrow(new RuntimeException("io error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.processExcel(file));
        assertTrue(ex.getMessage().contains("Failed to process Excel file"));
    }
}
