package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Department;
import edu.miu.eems.repo.IDepartmentRepo;
import java.sql.*;
import java.util.*;

public class JdbcDepartmentRepo implements IDepartmentRepo {
    @Override
    public Department add(Department d){
        // FIX: Changed from "upsert" to a pure INSERT
        String sql = "INSERT INTO department(id,name,location,budget) VALUES(?,?,?,?)";

        try(Connection c=DB.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,d.id());
            ps.setString(2,d.name());
            ps.setString(3,d.location());
            ps.setDouble(4,d.budget());
            ps.executeUpdate();
            return d;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Department d){
        String sql = "UPDATE department SET name = ?, location = ?, budget = ? " +
                "WHERE id = ?";

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.name());
            ps.setString(2, d.location());
            ps.setDouble(3, d.budget());
            ps.setInt(4, d.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // FIX: Reformatted for readability
    @Override
    public Optional<Department> findById(Integer id){
        String sql = "SELECT * FROM department WHERE id=?";
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){

            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    // FIX: Reformatted for readability
    @Override
    public List<Department> findAll(){
        String sql = "SELECT * FROM department ORDER BY id";
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement(sql)){

            try(ResultSet rs=ps.executeQuery()){
                List<Department> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    // FIX: Reformatted for readability
    @Override
    public boolean deleteById(Integer id){
        String sql = "DELETE FROM department WHERE id=?";
        try(Connection c=DB.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){

            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Department map(ResultSet rs) throws SQLException {
        return new Department(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getDouble("budget")
        );
    }
}