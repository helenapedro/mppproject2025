package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Employee;
import edu.miu.eems.repo.EmployeeRepo;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;
import static edu.miu.eems.repo.jdbc.JdbcUtil.getLocalDate;

public class JdbcEmployeeRepo implements EmployeeRepo {
    private Employee map(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("title"),
                getLocalDate(rs, "hire_date"),
                rs.getDouble("salary"),
                rs.getInt("dept_id")
        );
    }

    @Override
    public Employee add(Employee e){
        String sql = "INSERT INTO employee(id,name,title,hire_date,salary,dept_id) VALUES(?,?,?,?,?,?) " +
                "ON DUPLICATE KEY UPDATE name=VALUES(name), title=VALUES(title), hire_date=VALUES(hire_date), salary=VALUES(salary), dept_id=VALUES(dept_id)";

        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,e.id());
            ps.setString(2,e.name());
            ps.setString(3,e.title());
            ps.setDate(4, Date.valueOf(e.hireDate()));
            ps.setDouble(5,e.salary());
            ps.setInt(6,e.deptId());
            ps.executeUpdate();
            return e;
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Employee> findById(Integer id){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM employee WHERE id=?")){
            ps.setInt(1,id);
            try(ResultSet rs=ps.executeQuery()){
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Employee> findAll(){
        try(Connection c=DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM employee ORDER BY id")){
            try(ResultSet rs=ps.executeQuery()){
                List<Employee> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean deleteById(Integer id){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("DELETE FROM employee WHERE id=?")){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override public List<Employee> findByDepartment(int deptId){
        try(Connection c=DB.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT * FROM employee WHERE dept_id=? ORDER BY name")){
            ps.setInt(1,deptId);
            try(ResultSet rs=ps.executeQuery()){
                List<Employee> list=new ArrayList<>();
                while(rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Employee e) { // Renamed from 'add'
        // Use a standard UPDATE query
        String sql = "UPDATE employee SET name = ?, title = ?, hire_date = ?, salary = ?, dept_id = ? " +
                "WHERE id = ?";

        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            // Note the new parameter order to match the SQL query
            ps.setString(1, e.name());
            ps.setString(2, e.title());
            ps.setDate(3, Date.valueOf(e.hireDate()));
            ps.setDouble(4, e.salary());
            ps.setInt(5, e.deptId());

            // Set the id for the WHERE clause
            ps.setInt(6, e.id());

            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}