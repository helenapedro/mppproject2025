package edu.miu.eems.dao.support;

import edu.miu.eems.domain.*;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;


public final class RowMappers {
    private RowMappers() {}

    public static Employee employee(ResultSet rs) throws SQLException {
        Date d = rs.getDate("hire_date");
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("title"),
                d == null ? null : d.toLocalDate(), rs.getDouble("salary"), rs.getInt("dept_id"));
    }

    public static Department department(ResultSet rs) throws SQLException {
        return new Department(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getDouble("budget")
        );
    }

    public static Project project(ResultSet rs) throws SQLException {
        Date s = rs.getDate("start_date"),
                e = rs.getDate("end_date");
        return new Project(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                s == null ? null : s.toLocalDate(),
                e == null ? null : e.toLocalDate(),
                rs.getDouble("budget"),
                ProjectStatus.valueOf(rs.getString("status")),
                rs.getInt("dept_id")
        );
    }

    public static Client client(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("industry"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    public static EmployeeProject empProj(ResultSet rs) throws SQLException {
        return new EmployeeProject(
                rs.getInt("employee_id"),
                rs.getInt("project_id"),
                rs.getDouble("allocation_pct")
        );
    }
}
