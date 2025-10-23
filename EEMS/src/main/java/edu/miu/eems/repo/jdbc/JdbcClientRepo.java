package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Client;
import edu.miu.eems.repo.IClientRepo; // Make sure this matches your interface name

import java.sql.*;
import java.time.LocalDate; // <-- Add this import
import java.util.ArrayList; // <-- Add this import
import java.util.*;

public class JdbcClientRepo implements IClientRepo {
    private Client map(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("industry"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    @Override
    public Client add(Client c){
        String sql = "INSERT INTO client(id,name,industry,email,phone) VALUES(?,?,?,?,?)";
        try(Connection cn= DB.getConnection(); PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setInt(1,c.id());
            ps.setString(2,c.name());
            ps.setString(3,c.industry());
            ps.setString(4,c.email());
            ps.setString(5,c.phone());
            ps.executeUpdate();
            return c;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Client c) {
        String sql = "UPDATE client SET name = ?, industry = ?, email = ?, phone = ? " +
                "WHERE id = ?";
        try (Connection cn = DB.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.name());
            ps.setString(2, c.industry());
            ps.setString(3, c.email());
            ps.setString(4, c.phone());
            ps.setInt(5, c.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Client> findById(Integer id){
        String sql = "SELECT * FROM client WHERE id=?";
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next()? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Client> findAll(){
        String sql = "SELECT * FROM client ORDER BY id";
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            try(ResultSet rs=ps.executeQuery()){
                List<Client> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM client WHERE id=?";
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    // --- ADD THIS ENTIRE METHOD ---
    @Override
    public List<Client> findClientsWithProjectsEndingBy(LocalDate endDate) {
        // This single query JOINS all 3 tables efficiently in the database
        String sql = "SELECT DISTINCT c.* " +
                "FROM client c " +
                "JOIN client_project cp ON c.id = cp.client_id " +
                "JOIN project p ON cp.project_id = p.id " +
                "WHERE p.end_date IS NOT NULL AND p.end_date <= ?";

        List<Client> list = new ArrayList<>();
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs)); // Uses your existing map() helper
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}