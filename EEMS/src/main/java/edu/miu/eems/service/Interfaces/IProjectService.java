package edu.miu.eems.service.Interfaces;

import edu.miu.eems.domain.Project;

import java.util.List;
import java.util.Optional;

public interface IProjectService {
    double calculateProjectHRCost(int projectId);
    List<Project> getProjectsByDepartment(int deptId, String sortBy);
    void add(Project project);
    void update(Project ProjId);
    void delete(int ProjId);
    Optional<Project> findById(int pid);


}
