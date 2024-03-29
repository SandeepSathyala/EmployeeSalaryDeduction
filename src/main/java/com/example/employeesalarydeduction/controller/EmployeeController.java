
package com.example.employeesalarydeduction.controller;

import com.example.employeesalarydeduction.dto.EmployeeTaxDTO;
import com.example.employeesalarydeduction.model.Employee;
import com.example.employeesalarydeduction.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/tax-deduction")
    public ResponseEntity<List<EmployeeTaxDTO>> calculateTaxForCurrentFinancialYear() {
        List<EmployeeTaxDTO> employeeTaxDTOList = employeeService.calculateTaxForCurrentFinancialYear();
        return new ResponseEntity<>(employeeTaxDTOList, HttpStatus.OK);
    }
}
