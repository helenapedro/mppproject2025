package edu.miu.eems.dao.department;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.*;
import edu.miu.eems.dao.support.*;
import edu.miu.eems.domain.*;
import java.sql.*;
import java.util.*;

public class FindDepartmentByIdDao implements Dao<Department> {
    private final int id;
    private final List<Department> out = new ArrayList<>();

    public FindDepartmentByIdDao(int id) {
        this.id = id;
    }

    @Override
    public String getSql() {
        return "SELECT * FROM department WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        if (rs.next())
            out.add(RowMappers.department(rs));
    }

    @Override
    public List<Department> getResults() {
        return out;
    }
}