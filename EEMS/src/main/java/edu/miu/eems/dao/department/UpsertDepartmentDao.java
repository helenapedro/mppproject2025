package edu.miu.eems.dao.department;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.Department;
import java.sql.*;
import java.util.*;

public class UpsertDepartmentDao implements Dao<Integer> {
    private final Department d;

    public UpsertDepartmentDao(Department d){
        this.d=d;
    }

    @Override
    public String getSql(){
        return "INSERT INTO department(id,name,location,budget) VALUES(?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name), location=VALUES(location), budget=VALUES(budget)";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,d.id());
        ps.setString(2,d.name());
        ps.setString(3,d.location());
        ps.setDouble(4,d.budget());
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}