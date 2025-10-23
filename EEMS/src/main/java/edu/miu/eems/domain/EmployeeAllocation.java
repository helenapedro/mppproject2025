package edu.miu.eems.domain;


public record EmployeeAllocation(
        int employeeId,
        int projectId,
        double allocationPct,
        double employeeSalary // <-- The data from the 'employee' table
) {}