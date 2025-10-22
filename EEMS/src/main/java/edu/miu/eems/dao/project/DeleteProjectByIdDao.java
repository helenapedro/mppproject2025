package edu.miu.eems.dao.project;

import edu.miu.eems.dao.Dao;
import java.sql.*;
import java.util.*;

public class DeleteProjectByIdDao implements Dao<Integer> {
    private final int id;

    public DeleteProjectByIdDao(int id){
        this.id = id;
    }

    @Override
    public String getSql(){
        return "DELETE FROM project WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,id);
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}