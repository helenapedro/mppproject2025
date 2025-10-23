package edu.miu.eems.repo;

import edu.miu.eems.domain.EmployeeAllocation; // Import the new DTO
import edu.miu.eems.domain.EmployeeProject;
import java.util.List;

public interface IAllocationsRepo { // Or whatever your interface is called
    EmployeeProject add(EmployeeProject ep);
    List<EmployeeProject> findByProject(int projectId);
    List<EmployeeProject> findByEmployee(int empId);
    boolean delete(int empId, int projId);

    // This is the new, efficient method
    List<EmployeeAllocation> findEmployeeAllocationsByProject(int projectId);
}