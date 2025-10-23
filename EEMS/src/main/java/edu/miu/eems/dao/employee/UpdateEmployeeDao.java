package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
public class UpdateEmployeeDao implements Dao<Integer> {
    private final Employee e;

    public UpdateEmployeeDao(Employee e) {
        this.e = e;
    }


    @Override
    public String getSql() {
        return "UPDATE employee SET name = ?, title = ?, hire_date = ?, salary = ?, dept_id = ? " +
                "WHERE id = ?";
    }


    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        // Parameters for the SET clause
        ps.setString(1, e.name());
        ps.setString(2, e.title());
        ps.setDate(3, java.sql.Date.valueOf(e.hireDate()));
        ps.setDouble(4, e.salary());
        ps.setInt(5, e.deptId());

        // Parameter for the WHERE clause
        ps.setInt(6, e.id());
    }


    @Override
    public List<Integer> getResults() {
        return List.of();
    }
}