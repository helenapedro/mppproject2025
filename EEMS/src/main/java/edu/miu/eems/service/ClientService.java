package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import edu.miu.eems.repo.jdbc.JdbcClientProjectRepo;
import edu.miu.eems.repo.jdbc.JdbcClientRepo;
import edu.miu.eems.repo.jdbc.JdbcProjectRepo;
import edu.miu.eems.service.Interfaces.IClientService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ClientService implements IClientService {
    private final ClientRepo clients;
    private final ProjectRepo projects;
    private final ClientProjectRepo clientProjects;

    public ClientService() {
        this.clients = new JdbcClientRepo();
        this.projects = new JdbcProjectRepo();
        this.clientProjects = new JdbcClientProjectRepo();
    }

    // Task 3: clients with projects ending within N days
    @Override
    public List<Client> findClientsByUpcomingProjectDeadline(int days) {
        LocalDate limit = LocalDate.now().plusDays(days);

        Set<Integer> projectIds = projects.findAll().stream()
                .filter(p -> p.endDate() != null && !p.endDate().isAfter(limit))
                .map(Project::id)
                .collect(Collectors.toSet());

        if (projectIds.isEmpty()) return Collections.emptyList();

        Set<Integer> clientIds = projectIds.stream()
                .flatMap(pid -> clientProjects.findByProject(pid).stream())
                .map(ClientProject::clientId)
                .collect(Collectors.toSet());

        return clients.findAll().stream()
                .filter(c -> clientIds.contains(c.id()))
                .collect(Collectors.toList());
    }

    @Override
    public void addClient(Client c) {
        clients.add(c);
    }

    @Override
    public void deleteById(int cId) {
        clients.deleteById(cId);
    }

    @Override
    public void update(Client c) {
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
