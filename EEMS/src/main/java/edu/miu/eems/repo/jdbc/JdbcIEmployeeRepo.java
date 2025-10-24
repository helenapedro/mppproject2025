package edu.miu.eems.repo.jdbc;

import edu.miu.eems.db.DB;
import edu.miu.eems.domain.Employee;
import edu.miu.eems.repo.IEmployeeRepo;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import static edu.miu.eems.repo.jdbc.JdbcUtil.getLocalDate;

public class JdbcIEmployeeRepo implements IEmployeeRepo {
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
    public Employee add(Employee e) {
        String sql = "INSERT INTO employee(id,name,title,hire_date,salary,dept_id) VALUES(?,?,?,?,?,?)";

        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.id());
            ps.setString(2, e.name());
            ps.setString(3, e.title());
            ps.setDate(4, Date.valueOf(e.hireDate()));
            ps.setDouble(5, e.salary());
            ps.setInt(6, e.deptId());
            ps.executeUpdate();
            return e;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void update(Employee e) {
        String sql = "UPDATE employee SET name = ?, title = ?, hire_date = ?, salary = ?, dept_id = ? " +
                "WHERE id = ?";

        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.name());
            ps.setString(2, e.title());
            ps.setDate(3, Date.valueOf(e.hireDate()));
            ps.setDouble(4, e.salary());
            ps.setInt(5, e.deptId());
            ps.setInt(6, e.id());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Employee> findById(Integer id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee ORDER BY id";
        List<Employee> list = new ArrayList<>();

        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(map(rs));
                return list;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Employee> findByDepartment(int deptId) {
        String sql = "SELECT * FROM employee WHERE dept_id = ? ORDER BY name";
        List<Employee> list = new ArrayList<>();

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