package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.*;
import edu.miu.eems.repo.ProjectRepo;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import static edu.miu.eems.repo.jdbc.JdbcUtil.getLocalDate;
import static java.sql.Types.DATE;

public class JdbcProjectRepo implements ProjectRepo {
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
    public Project save(Project p){
        String sql = "INSERT INTO project(id,name,description,start_date,end_date,budget,status,dept_id) VALUES(?,?,?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), start_date=VALUES(start_date), end_date=VALUES(end_date), budget=VALUES(budget), status=VALUES(status), dept_id=VALUES(dept_id)";

        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,p.id());
            ps.setString(2,p.name());
            ps.setString(3,p.description());
            ps.setDate(4, java.sql.Date.valueOf(p.startDate()));

            if (p.endDate() == null)
                ps.setNull(5, DATE);
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
    public Optional<Project> findById(Integer id){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM project WHERE id=?")){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override public List<Project> findAll(){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM project ORDER BY id")){
            try(ResultSet rs = ps.executeQuery()){
                List<Project>
                        list=new ArrayList<>();
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
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM project WHERE id=?")){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> findByDepartment(int deptId){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM project WHERE dept_id=?")){
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
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM project WHERE status='ACTIVE'")){
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
}