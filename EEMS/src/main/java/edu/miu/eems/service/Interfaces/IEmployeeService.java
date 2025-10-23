package edu.miu.eems.service.Interfaces;

import edu.miu.eems.domain.Employee;

import java.util.Optional;

public interface IEmployeeService {
    void transferEmployeeToDepartment(int employeeId, int newDeptId);
    void add(Employee c);
    void deleteById(int cid);
    void update(Employee c);
    Optional<Employee> findById(int id);
}
