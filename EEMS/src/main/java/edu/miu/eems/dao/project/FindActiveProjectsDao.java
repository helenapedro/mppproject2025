package edu.miu.eems.dao.project;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Project;
import java.sql.*;
import java.util.*;

public class FindActiveProjectsDao implements Dao<Project> {
    private final List<Project> out = new ArrayList<>();

    @Override
    public String getSql(){
        return "SELECT * FROM project WHERE status='ACTIVE'";
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while(rs.next())
            out.add(RowMappers.project(rs));
    }

    @Override public List<Project> getResults(){
        return out;
    }
}

