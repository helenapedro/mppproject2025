package edu.miu.eems.dao.project;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Project;
import java.sql.*;
import java.util.*;

public class FindProjectByIdDao implements Dao<Project> {
    private final int id;
    private final List<Project> out = new ArrayList<>();

    public FindProjectByIdDao(int id){
        this.id=id;
    }

    @Override
    public String getSql(){
        return "SELECT * FROM project WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, id);
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        if(rs.next())
            out.add(RowMappers.project(rs));
    }

    @Override public List<Project> getResults(){
        return out;
    }
}