package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.*;

public class ProjectService {
    private final ProjectRepo projects; private final AllocationsRepo allocations; private final EmployeeRepo employees;
    public ProjectService(ProjectRepo p, AllocationsRepo a, EmployeeRepo e) { this.projects=p; this.allocations=a; this.employees=e; }

    // Task 1: calculateProjectHRCost(projectId) – salary per month * months * allocation%
    public double calculateProjectHRCost(int projectId) {
        Project prj = projects.findById(projectId).orElseThrow();
        LocalDate end = prj.endDate()==null? LocalDate.now() : prj.endDate();
        long months = Math.max(1, ChronoUnit.MONTHS.between(prj.startDate(), end));
        return allocations.findByProject(projectId).stream()
                .map(ep -> Map.entry(ep, employees.findById(ep.employeeId()).orElseThrow()))
                .mapToDouble(e -> (e.getValue().salary()/12.0) * months * (e.getKey().allocationPct()/100.0))
                .sum();
    }

    // Task 2: ACTIVE projects by department, sorted
    public List<Project> getProjectsByDepartment(int deptId, String sortBy) {
        Comparator<Project> cmp = switch (sortBy==null? "": sortBy.toLowerCase()) {
            case "budget" -> Comparator.comparingDouble(Project::budget).reversed();
            case "enddate" -> Comparator.comparing(Project::endDate, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(Project::name);
        };
        return projects.findByDepartment(deptId).stream()
                .filter(p -> p.status() == ProjectStatus.ACTIVE)
                .sorted(cmp)
                .collect(Collectors.toList());
    }
}
