package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao;
import java.sql.*;
import java.util.*;

public class DeleteAllocationDao implements Dao<Integer> {
    private final int empId, projId;

    public DeleteAllocationDao(int empId,int projId){
        this.empId=empId;
        this.projId=projId;
    }

    @Override
    public String getSql(){
        return "DELETE FROM employee_project WHERE employee_id=? AND project_id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,empId);
        ps.setInt(2,projId);
    }

    @Override
    public List<Integer> getResults(){
        return List.of();
    }
}
