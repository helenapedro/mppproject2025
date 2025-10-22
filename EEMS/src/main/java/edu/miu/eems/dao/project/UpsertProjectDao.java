package edu.miu.eems.dao.project;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.*;
import java.sql.*;
import java.util.*;

public class UpsertProjectDao implements Dao<Integer> {
    private final Project p;

    public UpsertProjectDao(Project p){
        this.p = p;
    }

    @Override
    public String getSql(){
        return "INSERT INTO project(id,name,description,start_date,end_date,budget,status,dept_id) VALUES(?,?,?,?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), start_date=VALUES(start_date), end_date=VALUES(end_date), budget=VALUES(budget), status=VALUES(status), dept_id=VALUES(dept_id)";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,p.id());
        ps.setString(2,p.name());
        ps.setString(3,p.description());
        ps.setDate(4, java.sql.Date.valueOf(p.startDate()));

        if(p.endDate() == null)
            ps.setNull(5, java.sql.Types.DATE);
        else
            ps.setDate(5, java.sql.Date.valueOf(p.endDate()));

        ps.setDouble(6,p.budget());
        ps.setString(7,p.status().name());
        ps.setInt(8,p.deptId());
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}
