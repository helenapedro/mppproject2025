package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import edu.miu.eems.service.Interfases.IProjectService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectService implements IProjectService {
    private final IProjectRepo projects;
    private final IAllocationsRepo allocations;

    public ProjectService(IProjectRepo projects, IAllocationsRepo allocations) {
        this.projects = projects;
        this.allocations = allocations;

    }

    @Override
    public double calculateProjectHRCost(int projectId) {
        Project prj = projects.findById(projectId).orElseThrow(() ->
                new IllegalArgumentException("Project with ID " + projectId + " not found"));

        LocalDate start = prj.startDate();
        LocalDate end = prj.endDate() != null ? prj.endDate() : LocalDate.now();
        if (start == null) throw new IllegalStateException("Project start date is missing");


        long calculatedMonths = (long) Math.ceil(ChronoUnit.DAYS.between(start, end) / 30.44);

        final long months = Math.max(1, calculatedMonths);

        return allocations.findEmployeeAllocationsByProject(projectId).stream()
                .mapToDouble(empAlloc -> {
                    double monthlySalary = empAlloc.employeeSalary() / 12.0;
                    double allocationFraction = empAlloc.allocationPct() / 100.0;
                    return monthlySalary * months * allocationFraction;
                })
                .sum();
    }

    @Override
    public List<Project> getProjectsByDepartment(int deptId, String sortBy) {
        return projects.findActiveByDepartmentSorted(deptId, sortBy);
    }

    @Override
    public void add(Project project) {
        projects.add(project);
    }

    @Override
    public void delete(int projId) {
        projects.deleteById(projId);
    }

    @Override
    public void update(Project project) {
        projects.update(project);
    }

    @Override
    public Optional<Project> findById(int pid) {
        return projects.findById(pid);
    }
}