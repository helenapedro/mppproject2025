package edu.miu.eems.service;


import edu.miu.eems.domain.Employee;
import edu.miu.eems.repo.IDepartmentRepo; // 2. Import Department repo
import edu.miu.eems.repo.IEmployeeRepo;
import edu.miu.eems.service.Interfases.IEmployeeService;

import java.util.Optional;

public class EmployeeService implements IEmployeeService {
    private final IEmployeeRepo employees;
    private final IDepartmentRepo departments; // 3. Add department repo for validation

    public EmployeeService(IEmployeeRepo employees, IDepartmentRepo departments) {
        this.employees = employees;
        this.departments = departments;
    }

    @Override
    public void transferEmployeeToDepartment(int employeeId, int newDeptId) {
        Employee emp = employees.findById(employeeId).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + employeeId + " not found"));

        departments.findById(newDeptId).orElseThrow(() ->
                new IllegalArgumentException("Department with ID " + newDeptId + " not found"));

        Employee updatedEmployee = new Employee(
                emp.id(),
                emp.name(),
                emp.title(),
                emp.hireDate(),
                emp.salary(),
                newDeptId // The only change
        );

        employees.update(updatedEmployee);
    }

    @Override
    public void add(Employee e) {
        employees.add(e);
    }

    @Override
    public void deleteById(int id) {
        // Check for existence *before* deleting
        employees.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + id + " not found"));
        employees.deleteById(id);
    }

    @Override
    public void update(Employee e) {
        // You should also validate existence on update
        employees.findById(e.id()).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + e.id() + " not found"));
        employees.update(e);
    }

    @Override
    public Optional<Employee> findById(int id) {
        return employees.findById(id);
    }
}