package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import edu.miu.eems.repo.jdbc.JdbcAllocationsRepo;
import edu.miu.eems.repo.jdbc.JdbcEmployeeRepo;
import edu.miu.eems.repo.jdbc.JdbcProjectRepo;
import edu.miu.eems.service.Interfases.IProjectService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ProjectService implements IProjectService {
    private final ProjectRepo projects;
    private final AllocationsRepo allocations;
    private final EmployeeRepo employees;

    public ProjectService() {
        this.projects = new JdbcProjectRepo();
        this.allocations = new JdbcAllocationsRepo();
        this.employees = new JdbcEmployeeRepo();
    }

    // Task 1: calculateProjectHRCost(projectId) – salary per month * months * allocation%
    @Override
    public double calculateProjectHRCost(int projectId) {
        Project prj = projects.findById(projectId).orElseThrow(() ->
                new IllegalArgumentException("Project with ID " + projectId + " not found"));

        LocalDate start = prj.startDate();
        LocalDate end = prj.endDate() != null ? prj.endDate() : LocalDate.now();

        if (start == null) throw new IllegalStateException("Project start date is missing");

        long months = Math.max(1, ChronoUnit.MONTHS.between(start, end));

        return allocations.findByProject(projectId).stream()
                .map(allocation -> Map.entry(allocation, employees.findById(allocation.employeeId()).orElseThrow()))
                .mapToDouble(entry -> {
                    double monthlySalary = entry.getValue().salary() / 12.0;
                    double allocationFraction = entry.getKey().allocationPct() / 100.0;
                    return monthlySalary * months * allocationFraction;
                })
                .sum();
    }

    // Task 2: ACTIVE projects by department, sorted
    @Override
    public List<Project> getProjectsByDepartment(int deptId, String sortBy) {
        Comparator<Project> comparator = switch (sortBy == null ? "" : sortBy.toLowerCase()) {
            case "budget" -> Comparator.comparingDouble(Project::budget).reversed();
            case "enddate" -> Comparator.comparing(Project::endDate, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(Project::name);
        };

        return projects.findByDepartment(deptId).stream()
                .filter(project -> project.status() == ProjectStatus.ACTIVE)
                .sorted(comparator)
                .collect(Collectors.toList());
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
