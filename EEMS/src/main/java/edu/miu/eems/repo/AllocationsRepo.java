package edu.miu.eems.repo;

import edu.miu.eems.domain.EmployeeProject;

import java.util.List;

public interface AllocationsRepo { EmployeeProject save(EmployeeProject ep);
    List<EmployeeProject> findByProject(int projectId);
    List<EmployeeProject> findByEmployee(int employeeId);
    boolean delete(int employeeId, int projectId);
}

