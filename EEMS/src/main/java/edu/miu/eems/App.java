package edu.miu.eems;

import edu.miu.eems.repo.*;
import edu.miu.eems.repo.jdbc.*;
import edu.miu.eems.service.*;
import edu.miu.eems.domain.*;
import edu.miu.eems.db.DB;
import java.sql.*;
import java.util.*;

public class App {
    public static void main(String[] args) {
        // 0) Show DB connectivity
        try (var c = DB.getConnection()) {
            System.out.println("Connected to: " + c.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("DB connection failed: " + e.getMessage());
            return;
        }

        // 1) Wire repositories
        ProjectRepo projectRepo = new JdbcProjectRepo();
        EmployeeRepo employeeRepo = new JdbcEmployeeRepo();
        ClientRepo clientRepo = new JdbcClientRepo();
        AllocationsRepo allocationsRepo = new JdbcAllocationsRepo();
        ClientProjectRepo clientProjectRepo = new JdbcClientProjectRepo();

        // 2) Services
        ProjectService projectSvc = new ProjectService(projectRepo, allocationsRepo, employeeRepo);
        EmployeeService employeeSvc = new EmployeeService(employeeRepo);
        ClientService clientSvc = new ClientService(clientRepo, projectRepo, clientProjectRepo);

        // 3) Demo outputs
        System.out.println("=== EEMS Demo (MySQL) ===");
        System.out.println("1) HR Cost for Project #10 = " + projectSvc.calculateProjectHRCost(10));
        System.out.println("2) ACTIVE Projects for Department #1 sorted by budget:");
        projectSvc.getProjectsByDepartment(1, "budget").forEach(p -> System.out.println("   - "+p.name()+" ($"+p.budget()+")"));
        System.out.println("3) Clients with deadlines within 60 days:");
        clientSvc.findClientsByUpcomingProjectDeadline(60).forEach(c -> System.out.println("   - "+c.name()));
        System.out.println("4) Transfer Employee #2 to Department #2");
        employeeSvc.transferEmployeeToDepartment(2,2);
        System.out.println("   Employee #2 transferred. Verify in DB.");
        System.out.println("Done.");
    }
}