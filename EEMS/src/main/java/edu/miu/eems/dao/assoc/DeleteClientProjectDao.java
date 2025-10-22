package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao;
import java.sql.*;
import java.util.*;

public class DeleteClientProjectDao implements Dao<Integer> {
    private final int clientId, projectId;

    public DeleteClientProjectDao(int clientId,int projectId){
        this.clientId=clientId;
        this.projectId=projectId;
    }

    @Override public String getSql(){
        return "DELETE FROM client_project WHERE client_id=? AND project_id=?";
    }

    @Override public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,clientId);
        ps.setInt(2,projectId);
    }

    @Override public List<Integer> getResults(){ return List.of(); }
}
