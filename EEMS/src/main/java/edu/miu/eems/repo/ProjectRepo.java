package edu.miu.eems.repo;

import edu.miu.eems.domain.Project;
import java.util.List;

public interface ProjectRepo extends Crud<Project,Integer> {
    List<Project> findByDepartment(int deptId);
    List<Project> findActive();
}

