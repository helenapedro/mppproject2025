package edu.miu.eems.service;

// 1. Remove ALL java.sql imports
// import edu.miu.eems.db.DB;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;

import edu.miu.eems.domain.Employee;
import edu.miu.eems.repo.IDepartmentRepo; // 2. Import Department repo
import edu.miu.eems.repo.IEmployeeRepo;
// import edu.miu.eems.repo.jdbc.JdbcIEmployeeRepo; // Remove specific implementation
import edu.miu.eems.service.Interfases.IEmployeeService;

import java.util.Optional;

public class EmployeeService implements IEmployeeService {
    private final IEmployeeRepo employees;
    private final IDepartmentRepo departments; // 3. Add department repo for validation

    // 4. Use Constructor Injection
    public EmployeeService(IEmployeeRepo employees, IDepartmentRepo departments) {
        this.employees = employees;
        this.departments = departments;
    }

    // 5. Rewrite transferEmployeeToDepartment to ONLY use repository methods
    @Override
    public void transferEmployeeToDepartment(int employeeId, int newDeptId) {
        // A. Validate the Employee exists
        Employee emp = employees.findById(employeeId).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + employeeId + " not found"));

        // B. Validate the NEW Department exists (as required by the PDF)
        departments.findById(newDeptId).orElseThrow(() ->
                new IllegalArgumentException("Department with ID " + newDeptId + " not found"));

        // C. Create the updated Employee object
        //    (Since Employee is a record, we create a new one with the changed field)
        Employee updatedEmployee = new Employee(
                emp.id(),
                emp.name(),
                emp.title(),
                emp.hireDate(),
                emp.salary(),
                newDeptId // The only change
        );

        // D. Ask the repository to persist the change.
        //    The repo handles all SQL. This single call is atomic.
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