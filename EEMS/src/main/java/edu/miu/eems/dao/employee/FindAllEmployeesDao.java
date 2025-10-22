package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Employee;
import java.sql.*;
import java.util.*;

public class FindAllEmployeesDao implements Dao<Employee> {
    private final List<Employee> out = new ArrayList<>();

    @Override
    public String getSql() {
        return "SELECT * FROM employee ORDER BY id";
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while (rs.next())
            out.add(RowMappers.employee(rs));
    }

    @Override public List<Employee> getResults() {
        return out;
    }
}