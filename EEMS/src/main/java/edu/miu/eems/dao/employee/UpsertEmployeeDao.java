package edu.miu.eems.dao.employee;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.Employee;
import java.sql.*;
import java.util.*;

public class UpsertEmployeeDao implements Dao<Integer> {
    private final Employee e;

    public UpsertEmployeeDao(Employee e){
        this.e=e;
    }

    @Override
    public String getSql(){
        return "INSERT INTO employee(id,name,title,hire_date,salary,dept_id) VALUES(?,?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name), title=VALUES(title), hire_date=VALUES(hire_date), salary=VALUES(salary), dept_id=VALUES(dept_id)";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,e.id());
        ps.setString(2,e.name());
        ps.setString(3,e.title());
        ps.setDate(4, java.sql.Date.valueOf(e.hireDate()));
        ps.setDouble(5,e.salary());
        ps.setInt(6,e.deptId());
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}
