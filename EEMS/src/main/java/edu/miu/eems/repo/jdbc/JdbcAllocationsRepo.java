package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.EmployeeProject;
import edu.miu.eems.repo.AllocationsRepo;
import java.sql.*;
import java.util.*;

public class JdbcAllocationsRepo implements AllocationsRepo {
    private EmployeeProject map(ResultSet rs) throws SQLException {
        return new EmployeeProject(
                rs.getInt("employee_id"),
                rs.getInt("project_id"),
                rs.getDouble("allocation_pct")
        );
    }

    @Override
    public EmployeeProject save(EmployeeProject ep){
        String sql = "INSERT INTO employee_project(employee_id,project_id,allocation_pct) VALUES(?,?,?) " +
                "ON DUPLICATE KEY UPDATE allocation_pct=VALUES(allocation_pct)";

        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,ep.employeeId());
            ps.setInt(2,ep.projectId());
            ps.setDouble(3,ep.allocationPct());
            ps.executeUpdate();
            return ep;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeProject> findByProject(int projectId){
        try(Connection c=DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM employee_project WHERE project_id=?")){
            ps.setInt(1,projectId);
            try(ResultSet rs=ps.executeQuery()){
                List<EmployeeProject> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeProject> findByEmployee(int empId){
        try(Connection c=DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM employee_project WHERE employee_id=?")){
            ps.setInt(1,empId);
            try(ResultSet rs=ps.executeQuery()){
                List<EmployeeProject> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int empId, int projId){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM employee_project WHERE employee_id=? AND project_id=?")){
            ps.setInt(1,empId);
            ps.setInt(2,projId);
            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}