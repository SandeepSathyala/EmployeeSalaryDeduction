package com.example.employeesalarydeduction.controller;

import com.example.employeesalarydeduction.dto.EmployeeTaxDTO;
import com.example.employeesalarydeduction.servicde.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees/tax")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;



    @GetMapping("/getEmployeeData")
    public ResponseEntity<?> calculateTaxForCurrentFinancialYear(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate == null || endDate == null) {
                String errorMessage = "Both 'startDate' and 'endDate' parameters are required.";
                logger.error(errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }

            List<EmployeeTaxDTO> employeeTaxDTOList = employeeService.calculateTaxForCurrentFinancialYear(startDate, endDate);
            return new ResponseEntity<>(employeeTaxDTOList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred while calculating tax: " + e.getMessage(), e);
            throw new RuntimeException("An error occurred while calculating tax: " + e.getMessage(), e);
        }
    }
}
