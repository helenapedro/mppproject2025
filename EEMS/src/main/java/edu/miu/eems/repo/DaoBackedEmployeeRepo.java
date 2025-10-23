package edu.miu.eems.repo;

import edu.miu.eems.dao.client.UpdateClientDao;
import edu.miu.eems.domain.*;
import edu.miu.eems.dao.*;
import edu.miu.eems.dao.employee.*;
import java.sql.*;
import java.util.*;

public class DaoBackedEmployeeRepo implements EmployeeRepo {
    private final DataAccess da = new DataAccessSystem();

    @Override
    public Employee add(Employee e){
        try {
            da.write(new UpsertEmployeeDao(e));
            return e;
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }
    @Override
    public void update(Employee e) { // Renamed from 'add'
        try {
            // You must use a DAO object that is built to *only*
            // run an UPDATE statement, not an "upsert".
            da.write(new UpdateEmployeeDao(e)); // Changed from UpsertEmployeeDao
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<Employee> findById(Integer id){
        var q = new FindEmployeeByIdDao(id);
        try {
            da.read(q);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return q.getResults().stream().findFirst();
    }

    @Override
    public List<Employee> findAll(){
        var q=new FindAllEmployeesDao();

        try {
            da.read(q);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return q.getResults();
    }

    @Override
    public boolean deleteById(Integer id){
        try {
            da.write(new DeleteEmployeeByIdDao(id));
            return true;
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override public List<Employee> findByDepartment(int deptId){
        var q = new FindEmployeesByDeptDao(deptId);
        try {
            da.read(q);
        } catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return q.getResults();
    }
}
