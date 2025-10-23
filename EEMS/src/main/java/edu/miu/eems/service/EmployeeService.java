package edu.miu.eems.service;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Employee;
import edu.miu.eems.repo.EmployeeRepo;
import edu.miu.eems.repo.jdbc.JdbcEmployeeRepo;
import edu.miu.eems.service.Interfases.IEmployeeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class EmployeeService implements IEmployeeService {
    private final EmployeeRepo employees;

    public EmployeeService() {
        this.employees = new JdbcEmployeeRepo();
    }

    // Task 4: transactional department transfer
    @Override
    public void transferEmployeeToDepartment(int employeeId, int newDeptId) {
        // Ensure employee exists
        employees.findById(employeeId).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + employeeId + " not found"));

        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE employee SET dept_id = ? WHERE id = ?")) {
                ps.setInt(1, newDeptId);
                ps.setInt(2, employeeId);
                ps.executeUpdate();
                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw new RuntimeException("Failed to transfer employee: " + ex.getMessage(), ex);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void add(Employee e) {
        employees.add(e);
    }

    @Override
    public void deleteById(int id) {
        employees.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Employee with ID " + id + " not found"));
        employees.deleteById(id);
    }

    @Override
    public void update(Employee e) {
        employees.update(e);
    }

    @Override
    public Optional<Employee> findById(int id) {
        return employees.findById(id);
    }
}
