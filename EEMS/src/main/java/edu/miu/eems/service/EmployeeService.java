package edu.miu.eems.service;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import java.sql.*;

public class EmployeeService {
    private final EmployeeRepo employees;

    public EmployeeService(EmployeeRepo repo){
        this.employees = repo;
    }

    // Task 4: transactional department transfer
    public void transferEmployeeToDepartment(int employeeId, int newDeptId) {
        employees.findById(employeeId).orElseThrow();

        try (Connection c = DB.getConnection()) {
            c.setAutoCommit(false);
            try (
                    PreparedStatement ps = c.prepareStatement("UPDATE employee SET dept_id=? WHERE id=?")) {
                        ps.setInt(1, newDeptId);
                        ps.setInt(2, employeeId);
                        ps.executeUpdate();
                    }
                    c.commit();
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}