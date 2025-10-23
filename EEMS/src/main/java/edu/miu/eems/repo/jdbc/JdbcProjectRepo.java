package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.*;
import edu.miu.eems.repo.IProjectRepo;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import static edu.miu.eems.repo.jdbc.JdbcUtil.getLocalDate;
import java.sql.Types; // Added missing import

public class JdbcProjectRepo implements IProjectRepo {
    private Project map(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                getLocalDate(rs, "start_date"),
                getLocalDate(rs, "end_date"),
                rs.getDouble("budget"),
                ProjectStatus.valueOf(rs.getString("status")),
                rs.getInt("dept_id"));
    }

    @Override
    public Project add(Project p){
        // Changed to a pure INSERT
        String sql = "INSERT INTO project(id,name,description,start_date,end_date,budget,status,dept_id) VALUES(?,?,?,?,?,?,?,?)";

        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,p.id());
            ps.setString(2,p.name());
            ps.setString(3,p.description());
            ps.setDate(4, java.sql.Date.valueOf(p.startDate()));

            if (p.endDate() == null)
                ps.setNull(5, Types.DATE);
            else
                ps.setDate(5, Date.valueOf(p.endDate()));

            ps.setDouble(6,p.budget());
            ps.setString(7,p.status().name());
            ps.setInt(8,p.deptId());
            ps.executeUpdate();
            return p;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Project p) {
        String sql = "UPDATE project SET name = ?, description = ?, start_date = ?, end_date = ?, " +
                "budget = ?, status = ?, dept_id = ? " +
                "WHERE id = ?";

        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.name());
            ps.setString(2, p.description());
            ps.setDate(3, Date.valueOf(p.startDate()));

            if (p.endDate() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(p.endDate()));
            }

            ps.setDouble(5, p.budget());
            ps.setString(6, p.status().name());
            ps.setInt(7, p.deptId());
            ps.setInt(8, p.id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Project> findById(Integer id){
        String sql = "SELECT * FROM project WHERE id = ?";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override public List<Project> findAll(){
        String sql = "SELECT * FROM project ORDER BY id";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery()){
                List<Project> list=new ArrayList<>();
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
        String sql = "DELETE FROM project WHERE id = ?";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Project> findByDepartment(int deptId){
        String sql = "SELECT * FROM project WHERE dept_id = ?";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,deptId);
            try(ResultSet rs = ps.executeQuery()){
                List<Project> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> findActive(){
        String sql = "SELECT * FROM project WHERE status = 'ACTIVE'";
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            try(ResultSet rs=ps.executeQuery()){
                List<Project> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Project> findActiveByDepartmentSorted(int deptId, String sortBy) {
        String sortColumn = switch (sortBy == null ? "" : sortBy.toLowerCase()) {
            case "budget" -> "budget DESC";
            case "enddate" -> "end_date";
            default -> "name";
        };

        String sql = "SELECT * FROM project WHERE dept_id = ? AND status = 'ACTIVE' ORDER BY " + sortColumn;

        List<Project> list = new ArrayList<>();
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, deptId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}