package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Department;
import edu.miu.eems.repo.DepartmentRepo;
import java.sql.*;
import java.util.*;

public class JdbcDepartmentRepo implements DepartmentRepo {
    @Override
    public Department add(Department d){
        String sql = "INSERT INTO department(id,name,location,budget) VALUES(?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), location=VALUES(location), budget=VALUES(budget)";

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

            // Note the new parameter order to match the SQL query
            ps.setString(1, d.name());
            ps.setString(2, d.location());
            ps.setDouble(3, d.budget());

            // Set the id for the WHERE clause
            ps.setInt(4, d.id());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Department> findById(Integer id){
        try(
                Connection c=DB.getConnection();
                PreparedStatement ps=c.prepareStatement("SELECT * FROM department WHERE id=?")){
                    ps.setInt(1,id);
                    try(ResultSet rs=ps.executeQuery()){
                        return rs.next() ? Optional.of(map(rs)) : Optional.empty();
                    }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override public List<Department> findAll(){
        try(Connection c=DB.getConnection();
            PreparedStatement ps=c.prepareStatement("SELECT * FROM department ORDER BY id")){
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

    @Override
    public boolean deleteById(Integer id){
        try(Connection c=DB.getConnection();
                PreparedStatement ps = c.prepareStatement("DELETE FROM department WHERE id=?")){
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