package edu.miu.eems.repo;

import edu.miu.eems.domain.Employee;

import java.util.List;

public interface EmployeeRepo extends Crud<Employee,Integer> {
    List<Employee> findByDepartment(int deptId);
}
