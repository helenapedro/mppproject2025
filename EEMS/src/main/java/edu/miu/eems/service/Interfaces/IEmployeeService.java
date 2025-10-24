package edu.miu.eems.service.Interfaces;

import edu.miu.eems.domain.Employee; // Client import removed
import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    void transferEmployeeToDepartment(int employeeId, int newDeptId);
    void add(Employee e); // Renamed parameter from 'c' to 'e'
    void deleteById(int id); // Renamed parameter from 'cid' to 'id'
    void update(Employee e); // Renamed parameter from 'c' to 'e'
    Optional<Employee> findById(int id);
}