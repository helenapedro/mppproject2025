package edu.miu.eems.repo;

import edu.miu.eems.domain.Project;
import java.util.List;

public interface IProjectRepo extends ICrud<Project,Integer> {
    List<Project> findByDepartment(int deptId);
    List<Project> findActive();

    List<Project> findActiveByDepartmentSorted(int deptId, String sortBy);
}