package edu.miu.eems.repo;

import edu.miu.eems.domain.Project;
import java.util.List;

public interface IProjectRepo extends ICrud<Project,Integer> {
    // Kept original methods
    List<Project> findByDepartment(int deptId);
    List<Project> findActive();

    // Added new efficient method for the service layer
    List<Project> findActiveByDepartmentSorted(int deptId, String sortBy);
}