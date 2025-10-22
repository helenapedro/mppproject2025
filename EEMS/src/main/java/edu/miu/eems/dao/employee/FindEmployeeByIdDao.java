package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Employee;
import java.sql.*;
import java.util.*;

public class FindEmployeeByIdDao implements Dao<Employee> {
    private final int id;
    private final List<Employee> out = new ArrayList<>();

    public FindEmployeeByIdDao(int id) {
        this.id = id;
    }

    @Override
    public String getSql() {
        return "SELECT * FROM employee WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        if (rs.next())
            out.add(RowMappers.employee(rs));
    }

    @Override public List<Employee> getResults() {
        return out;
    }
}