package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import java.sql.*;
import java.util.*;

public class DeleteEmployeeByIdDao implements Dao<Integer> {
    private final int id;

    public DeleteEmployeeByIdDao(int id){
        this.id=id;
    }

    @Override
    public String getSql(){
        return "DELETE FROM employee WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,id);
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}