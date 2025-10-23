package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.ClientProject;
import edu.miu.eems.repo.IClientProjectRepo;

import java.sql.*;
import java.util.*;

public class JdbcClientProjectRepo implements IClientProjectRepo {
    private ClientProject map(ResultSet rs) throws SQLException {
        return new ClientProject(
                rs.getInt("client_id"),
                rs.getInt("project_id")
        );
    }

    @Override
    public ClientProject add(ClientProject cp){
        // FIX: Changed from "upsert" to a pure INSERT
        String sql = "INSERT INTO client_project(client_id,project_id) VALUES(?,?)";

        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,cp.clientId());
            ps.setInt(2,cp.projectId());
            ps.executeUpdate();
            return cp;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientProject> findByClient(int clientId){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM client_project WHERE client_id=?")){
            ps.setInt(1,clientId);
            try(ResultSet rs=ps.executeQuery()){
                List<ClientProject> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientProject> findByProject(int projectId){
        try(Connection c=DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM client_project WHERE project_id=?")){
            ps.setInt(1,projectId);
            try(ResultSet rs=ps.executeQuery()){
                List<ClientProject> list = new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int clientId, int projectId){
        try(Connection c=DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM client_project WHERE client_id=? AND project_id=?")){
            ps.setInt(1,clientId);
            ps.setInt(2,projectId);
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}