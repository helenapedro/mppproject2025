package edu.miu.eems.dao.assoc;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.ClientProject;
import java.sql.*;
import java.util.*;

public class FindClientProjectByProjectDao implements Dao<ClientProject> {
    private final int projectId;
    private final List<ClientProject> out = new ArrayList<>();

    public FindClientProjectByProjectDao(int projectId){
        this.projectId = projectId;
    }

    @Override public String getSql(){
        return "SELECT * FROM client_project WHERE project_id=?";
    }

    @Override public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1, projectId);
    }

    @Override public void unpack(ResultSet rs) throws SQLException {
        while(rs.next())
            out.add(new ClientProject(
                    rs.getInt("client_id"),
                    rs.getInt("project_id"))
            );
    }

    @Override public List<ClientProject> getResults(){
        return out;
    }
}
