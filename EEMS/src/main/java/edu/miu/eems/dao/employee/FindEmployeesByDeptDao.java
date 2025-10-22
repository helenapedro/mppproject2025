package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Employee;
import java.sql.*;
import java.util.*;

public class FindEmployeesByDeptDao implements Dao<Employee> {
    private final int deptId; private final List<Employee> out = new ArrayList<>();

    public FindEmployeesByDeptDao(int deptId) {
        this.deptId = deptId;
    }

    @Override
    public String getSql() {
        return "SELECT * FROM employee WHERE dept_id=? ORDER BY name";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, deptId);
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while (rs.next())
            out.add(RowMappers.employee(rs));
    }

    @Override
    public List<Employee> getResults() {
        return out;
    }
}