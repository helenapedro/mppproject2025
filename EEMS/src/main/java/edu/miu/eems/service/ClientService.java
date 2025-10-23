package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
// REMOVE: All hard-coded jdbc imports
// import edu.miu.eems.repo.jdbc.JdbcClientProjectRepo;
// import edu.miu.eems.repo.jdbc.JdbcClientRepo;
// import edu.miu.eems.repo.jdbc.JdbcProjectRepo;
import edu.miu.eems.service.Interfases.IClientService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ClientService implements IClientService {
    private final IClientRepo clients;
    private final IProjectRepo projects;
    private final IClientProjectRepo clientProjects;

    // --- FIX 1: Use Constructor Injection ---
    // Instead of newing up concrete classes, accept the interfaces.
    public ClientService(IClientRepo clients, IProjectRepo projects, IClientProjectRepo clientProjects) {
        this.clients = clients;
        this.projects = projects;
        this.clientProjects = clientProjects;
    }

    // --- FIX 2: Efficient Database-Driven Logic ---
    @Override
    public List<Client> findClientsByUpcomingProjectDeadline(int days) {
        LocalDate limit = LocalDate.now().plusDays(days);

        // All the inefficient Java stream processing is replaced by
        // a single call to a new, efficient repository method.
        // All filtering and joining now happens in the database.
        return clients.findClientsWithProjectsEndingBy(limit);
    }

    @Override
    public void addClient(Client c) {
        // You could add business validation here, e.g.,
        // if (clients.findById(c.id()).isPresent()) {
        //     throw new IllegalArgumentException("Client ID already exists.");
        // }
        clients.add(c);
    }

    // --- FIX 3: Added Validation ---
    @Override
    public void deleteById(int cId) {
        // Add validation to ensure the client exists before deleting
        clients.findById(cId).orElseThrow(() ->
                new IllegalArgumentException("Cannot delete: Client with ID " + cId + " not found."));
        clients.deleteById(cId);
    }

    @Override
    public void update(Client c) {
        // Add validation to ensure the client exists before updating
        clients.findById(c.id()).orElseThrow(() ->
                new IllegalArgumentException("Cannot update: Client with ID " + c.id() + " not found."));
        clients.update(c);
    }

    @Override
    public List<Client> findAllClients() {
        return clients.findAll();
    }

    @Override
    public Optional<Client> findById(int id) {
        return clients.findById(id);
    }
}