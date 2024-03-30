package com.example.employeesalarydeduction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTaxDTO {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private LocalDate doj;
    private double yearlySalary;
    private double taxAmount;
    private double cessAmount;
//    private double lossOfPayPerDay;
    private double salaryAfterTaxYearly;
    private double salary;
}
