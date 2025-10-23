package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Client;
import edu.miu.eems.repo.ClientRepo;

import java.sql.*;
import java.util.*;

public class JdbcClientRepo implements ClientRepo {
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
        String sql = "INSERT INTO client(id,name,industry,email,phone) VALUES(?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), industry=VALUES(industry), email=VALUES(email), phone=VALUES(phone)";
        try(Connection cn= DB.getConnection(); PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setInt(1,c.id());
            ps.setString(2,c.name());
            ps.setString(3,c.industry()); ps.setString(4,c.email()); ps.setString(5,c.phone());
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
            ps.setInt(5, c.id()); // 'id' is used in the WHERE clause

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public Optional<Client> findById(Integer id){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM client WHERE id=?")){ ps.setInt(1,id); try(ResultSet rs=ps.executeQuery()){ return rs.next()? Optional.of(map(rs)) : Optional.empty(); } } catch(SQLException e){ throw new RuntimeException(e);} }
    @Override public List<Client> findAll(){ try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM client ORDER BY id")){ try(ResultSet rs=ps.executeQuery()){ List<Client> list=new ArrayList<>(); while(rs.next()) list.add(map(rs)); return list; } } catch(SQLException e){ throw new RuntimeException(e);} }
    @Override public boolean deleteById(Integer id)
    {
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM client WHERE id=?")){ ps.setInt(1,id); return ps.executeUpdate()>0; } catch(SQLException e){ throw new RuntimeException(e);} }
}
