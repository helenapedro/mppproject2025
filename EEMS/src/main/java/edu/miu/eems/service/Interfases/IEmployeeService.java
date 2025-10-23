package edu.miu.eems.service.Interfases;

import edu.miu.eems.domain.Client;
import edu.miu.eems.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    void transferEmployeeToDepartment(int employeeId, int newDeptId);
    void add(Employee c);
    void deleteById(int cid);
    void update(Employee c);
    Optional<Employee> findById(int id);
}
