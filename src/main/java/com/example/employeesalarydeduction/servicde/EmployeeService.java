package com.example.employeesalarydeduction.servicde;

import com.example.employeesalarydeduction.dto.EmployeeTaxDTO;
import com.example.employeesalarydeduction.model.Employee;
import com.example.employeesalarydeduction.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    public Employee createEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            logger.error("An error occurred while creating employee: " + e.getMessage(), e);
            throw new RuntimeException("An error occurred while creating employee: " + e.getMessage(), e);
        }
    }
    public List<EmployeeTaxDTO> calculateTaxForCurrentFinancialYear(LocalDate startDate, LocalDate endDate) {
        List<Employee> employees = employeeRepository.findByDojBetween(startDate, endDate);
        List<EmployeeTaxDTO> employeeTaxDTOList = new ArrayList<>();
        try {
            for (Employee employee : employees) {
//                double yearlySalary = calculateYearlySalary(employee);
                double yearlySalary = 0;
                double taxAmount = calculateTaxAmount(yearlySalary);
                double cessAmount = calculateCessAmount(yearlySalary);

                EmployeeTaxDTO employeeTaxDTO = new EmployeeTaxDTO();
                employeeTaxDTO.setEmployeeId(employee.getEmployeeId());
                employeeTaxDTO.setFirstName(employee.getFirstName());
                employeeTaxDTO.setLastName(employee.getLastName());
                employeeTaxDTO.setYearlySalary(yearlySalary);
                employeeTaxDTO.setTaxAmount(taxAmount);
                employeeTaxDTO.setCessAmount(cessAmount);

                employeeTaxDTOList.add(employeeTaxDTO);
            }
        } catch (Exception e) {
            logger.error("An error occurred while calculating tax: " + e.getMessage(), e);
            throw new RuntimeException("An error occurred while calculating tax: " + e.getMessage(), e);
        }
        return employeeTaxDTOList;
    }



    private double calculateTaxAmount(double yearlySalary) {
        double taxAmount = 0.0;
        if (yearlySalary > 2500000) {
            taxAmount += (yearlySalary - 2500000) * 0.02;
            yearlySalary = 2500000;
        }
        if (yearlySalary > 1000000) {
            taxAmount += (yearlySalary - 1000000) * 0.2;
            yearlySalary = 1000000;
        }
        if (yearlySalary > 500000) {
            taxAmount += (yearlySalary - 500000) * 0.1;
            yearlySalary = 500000;
        }
        if (yearlySalary > 250000) {
            taxAmount += (yearlySalary - 250000) * 0.05;
        }
        return taxAmount;
    }

    private double calculateCessAmount(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return (yearlySalary - 2500000) * 0.02;
        }
        return 0.0;
    }


}
