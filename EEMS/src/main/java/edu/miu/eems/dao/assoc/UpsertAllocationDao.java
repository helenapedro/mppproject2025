package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.EmployeeProject;
import java.sql.*;
import java.util.*;

public class UpsertAllocationDao implements Dao<Integer> {
    private final EmployeeProject ep;

    public UpsertAllocationDao(EmployeeProject ep){
        this.ep = ep;
    }

    @Override
    public String getSql(){
        return "INSERT INTO employee_project(employee_id,project_id,allocation_pct) VALUES(?,?,?) " +
            "ON DUPLICATE KEY UPDATE allocation_pct=VALUES(allocation_pct)";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,ep.employeeId());
        ps.setInt(2,ep.projectId());
        ps.setDouble(3,ep.allocationPct());
    }

    @Override
    public List<Integer> getResults(){
        return List.of();
    }
}