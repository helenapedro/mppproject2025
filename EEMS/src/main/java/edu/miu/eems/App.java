package edu.miu.eems;

import edu.miu.eems.repo.*;
import edu.miu.eems.repo.jdbc.*;
import edu.miu.eems.service.*;
import edu.miu.eems.db.DB;
import edu.miu.eems.service.Interfaces.IClientService;
import edu.miu.eems.service.Interfaces.IEmployeeService;
import edu.miu.eems.service.Interfaces.IProjectService;

import java.util.Scanner;

public class App {


    private Scanner sc;

    private final IDepartmentRepo departmentRepo;
    private final IEmployeeRepo employeeRepo;
    private final IProjectRepo projectRepo;
    private final IClientRepo clientRepo;
    private final IAllocationsRepo allocationsRepo;
    private final IClientProjectRepo clientProjectRepo;

    private final IClientService clientSvc;
    private final IProjectService projectSvc;
    private final IEmployeeService employeeSvc;

    public App() {
        // Initialize Repositories first
        this.departmentRepo = new JdbcDepartmentRepo();
        this.employeeRepo = new JdbcIEmployeeRepo();
        this.projectRepo = new JdbcProjectRepo();
        this.clientRepo = new JdbcClientRepo();
        this.allocationsRepo = new JdbcAllocationsRepo();
        this.clientProjectRepo = new JdbcClientProjectRepo();

        this.employeeSvc = new EmployeeService(employeeRepo, departmentRepo);
        this.clientSvc = new ClientService(clientRepo, projectRepo, clientProjectRepo);
        this.projectSvc = new ProjectService(projectRepo, allocationsRepo);
    }

    public static void main(String[] args) {
        System.out.println("Starting...");
        App app = new App();
        app.run();
    }

    private void run() {
        try (var c = DB.getConnection()) {
            System.out.println("Connected to: " + c.getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("DB connection failed: " + e.getMessage());
            return;
        }

        sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            mainMenu();
            running = handleMainMenu();
        }

        exit();
    }

    private void mainMenu() {
        System.out.println("\nEmployment Management System");
        System.out.println("1. Calculate Project HR Cost");
        System.out.println("2. Get Projects By Department");
        System.out.println("3. Find Clients By Upcoming Project Deadline");
        System.out.println("4. Transfer Employee");
        System.out.println("5. Quit");
        System.out.print("Pick a number from 1 to 5: ");
    }

    private boolean handleMainMenu() {
        String input = sc.nextLine();

        try {
            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    calculateProjectHRCost();
                    return handleSubMenu(); // Ask to continue
                case 2:
                    getProjectsByDepartment();
                    return handleSubMenu(); // Ask to continue
                case 3:
                    findClientsByUpcomingProjectDeadline();
                    return handleSubMenu(); // Ask to continue
                case 4:
                    transferEmployeeToDepartment();
                    return handleSubMenu(); // Ask to continue
                case 5:
                    return false; // Signal the run() loop to stop
                default:
                    System.out.println("Invalid choice. Please enter a number from the menu.");
                    return true; // Continue the run() loop
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return true; // Continue the run() loop
        }
    }

    private boolean handleSubMenu() {
        System.out.println("\n1. Return to main menu");
        System.out.println("2. Quit");
        System.out.print("Pick a number from 1 to 2: ");
        String input = sc.nextLine();

        try {
            int choice = Integer.parseInt(input);
            return choice == 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Returning to main menu.");
            return true;
        }
    }

    private void exit() {
        System.out.println("Goodbye.");
        if (sc != null) sc.close();
    }

    private void calculateProjectHRCost() {
        try {
            System.out.printf("HR Cost for Project #10 = %.2f%n",
                    projectSvc.calculateProjectHRCost(10));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void getProjectsByDepartment() {
        try {
            System.out.println("ACTIVE Projects for Department #1 sorted by budget:");
            projectSvc.getProjectsByDepartment(1, "budget")
                    .forEach(p -> System.out.printf("   - %s ($%.2f)%n", p.name(), p.budget()));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void findClientsByUpcomingProjectDeadline() {
        try {
            System.out.println("Clients with deadlines within 60 days:");
            clientSvc.findClientsByUpcomingProjectDeadline(60)
                    .forEach(c -> System.out.println("   - " + c.name()));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void transferEmployeeToDepartment() {
        try {
            System.out.println("Transfer Employee #2 to Department #2");
            employeeSvc.transferEmployeeToDepartment(2, 2);
            System.out.println("Employee #2 transferred. Verify in DB.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}