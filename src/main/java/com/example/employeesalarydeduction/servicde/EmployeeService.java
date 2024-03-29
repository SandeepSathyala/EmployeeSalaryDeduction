package com.example.employeesalarydeduction.servicde;

import com.example.employeesalarydeduction.dto.EmployeeTaxDTO;
import com.example.employeesalarydeduction.model.Employee;
import com.example.employeesalarydeduction.repo.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee) {
        validateEmployeeData(employee);
        return employeeRepository.save(employee);
    }

    public List<EmployeeTaxDTO> calculateTaxForCurrentFinancialYear() {
        LocalDate startDate = LocalDate.of(LocalDate.now().minusYears(1).getYear(), 4, 1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 3, 31);

        return employeeRepository.findAll().stream()
                .map(employee -> {
                    double yearlySalary = calculateYearlySalary(employee, startDate, endDate);
                    double taxAmount = calculateTaxAmount(yearlySalary);
                    double cessAmount = calculateCessAmount(yearlySalary);

                    return new EmployeeTaxDTO(
                            employee.getEmployeeId(),
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getDoj(),
                            yearlySalary,
                            taxAmount,
                            cessAmount
                    );
                })
                .collect(Collectors.toList());
    }

    private double calculateYearlySalary(Employee employee, LocalDate startDate, LocalDate endDate) {
        double totalSalary = 0.0;
        LocalDate doj = employee.getDoj();
        LocalDate startMonth = doj.isAfter(startDate) ? doj : startDate;
        LocalDate endMonth = endDate.isBefore(LocalDate.now()) ? endDate : LocalDate.now().minusDays(1);

        if (doj.getMonthValue() != startMonth.getMonthValue()) {
            int daysWorked = doj.lengthOfMonth() - doj.getDayOfMonth() + 1;
            totalSalary += (employee.getSalary() / doj.lengthOfMonth()) * daysWorked;
            startMonth = startMonth.plusMonths(1);
        }

        if (startMonth.isBefore(endMonth)) {
            long totalMonths = ChronoUnit.MONTHS.between(startMonth.withDayOfMonth(1), endMonth.withDayOfMonth(1)) + 1;
            totalSalary += employee.getSalary() * totalMonths;
        }

        return totalSalary;
    }

    private double calculateTaxAmount(double yearlySalary) {
        if (yearlySalary <= 250000) return 0.0;
        else if (yearlySalary <= 500000) return (yearlySalary - 250000) * 0.05;
        else if (yearlySalary <= 1000000) return 12500 + (yearlySalary - 500000) * 0.1;
        else return 62500 + (yearlySalary - 1000000) * 0.2;
    }

    private double calculateCessAmount(double yearlySalary) {
        return yearlySalary > 2500000 ? (yearlySalary - 2500000) * 0.02 : 0.0;
    }

    private void validateEmployeeData(Employee employee) {
        if (employee.getEmployeeId() != null)
            throw new IllegalArgumentException("Employee ID should not be provided.");
        if (employee.getFirstName() == null || employee.getFirstName().isEmpty())
            throw new IllegalArgumentException("First Name is required.");
        if (employee.getLastName() == null || employee.getLastName().isEmpty())
            throw new IllegalArgumentException("Last Name is required.");
        if (employee.getEmail() == null || employee.getEmail().isEmpty())
            throw new IllegalArgumentException("Email is required.");
        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().isEmpty())
            throw new IllegalArgumentException("Phone Number(s) is required.");
        if (employee.getDoj() == null)
            throw new IllegalArgumentException("Date of Joining is required.");
        if (employee.getSalary() <= 0)
            throw new IllegalArgumentException("Salary should be greater than zero.");
    }
}
