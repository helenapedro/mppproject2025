package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.ClientProject;
import java.sql.*;
import java.util.*;

public class UpsertClientProjectDao implements Dao<Integer> {
    private final ClientProject cp;

    public UpsertClientProjectDao(ClientProject cp){
        this.cp = cp;
    }

    @Override public String getSql(){
        return "INSERT INTO client_project(client_id,project_id) VALUES(?,?) " +
            "ON DUPLICATE KEY UPDATE client_id=VALUES(client_id), project_id=VALUES(project_id)";
    }

    @Override public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,cp.clientId());
        ps.setInt(2,cp.projectId());
    }

    @Override public List<Integer> getResults(){ return List.of(); }
}