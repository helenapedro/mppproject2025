package edu.miu.eems.dao.department;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Department;
import java.sql.*;
import java.util.*;

public class FindAllDepartmentsDao implements Dao<Department> {
    private final List<Department> out = new ArrayList<>();

    @Override
    public String getSql() {
        return "SELECT * FROM department ORDER BY id";
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while (rs.next())
            out.add(RowMappers.department(rs));
    }

    @Override
    public List<Department> getResults() {
        return out;
    }
}
