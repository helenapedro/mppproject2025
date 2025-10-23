package edu.miu.eems;

import edu.miu.eems.repo.*;
import edu.miu.eems.repo.jdbc.*;
import edu.miu.eems.service.*;
import edu.miu.eems.db.DB;
import edu.miu.eems.service.Interfases.IClientService;
import edu.miu.eems.service.Interfases.IEmployeeService;
import edu.miu.eems.service.Interfases.IProjectService;

import java.util.Scanner;

public class App {
    private Scanner sc;
    private final IClientService clientSvc = new ClientService();
    private final IProjectService projectSvc = new ProjectService();
    private final IEmployeeService employeeSvc = new EmployeeService();

    public static void main(String[] args) {
        System.out.println("Starting...");
        App app = new App();
        app.run();
    }

    private void run() {
        // Show DB connectivity
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
                case 1 -> calculateProjectHRCost();
                case 2 -> getProjectsByDepartment();
                case 3 -> findClientsByUpcomingProjectDeadline();
                case 4 -> transferEmployeeToDepartment();
                case 5 -> exit();
                default -> System.out.println("Invalid choice. Please enter a number from the menu.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }

        return handleSubMenu();
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
        System.out.printf("HR Cost for Project #10 = %.2f%n",
                projectSvc.calculateProjectHRCost(10));
    }

    private void getProjectsByDepartment() {
        System.out.println("ACTIVE Projects for Department #1 sorted by budget:");
        projectSvc.getProjectsByDepartment(1, "budget")
                .forEach(p -> System.out.printf("   - %s ($%.2f)%n", p.name(), p.budget()));
    }

    private void findClientsByUpcomingProjectDeadline() {
        System.out.println("Clients with deadlines within 60 days:");
        clientSvc.findClientsByUpcomingProjectDeadline(60)
                .forEach(c -> System.out.println("   - " + c.name()));
    }

    private void transferEmployeeToDepartment() {
        System.out.println("Transfer Employee #2 to Department #2");
        employeeSvc.transferEmployeeToDepartment(2, 2);
        System.out.println("Employee #2 transferred. Verify in DB.");
    }
}
