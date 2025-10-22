package edu.miu.eems.service;

import edu.miu.eems.domain.*;
import edu.miu.eems.repo.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

public class ClientService {
    private final ClientRepo clients;
    private final ProjectRepo projects;
    private final ClientProjectRepo clientProjects;
    public ClientService(ClientRepo c, ProjectRepo p, ClientProjectRepo cp){
        this.clients = c;
        this.projects = p;
        this.clientProjects = cp;
    }

    // Task 3: clients with projects ending within N days
    public List<Client> findClientsByUpcomingProjectDeadline(int days) {
        LocalDate limit = LocalDate.now().plusDays(days);
        // Collect projectIds ending before limit
        Set<Integer> projectIds = projects.findAll().stream()
                .filter(p -> p.endDate()!=null && !p.endDate().isAfter(limit))
                .map(Project::id)
                .collect(Collectors.toSet());

        if(projectIds.isEmpty()) return List.of();
        // Resolve client ids via association repo
        Set<Integer> clientIds = projectIds.stream()
                .flatMap(pid -> clientProjects.findByProject(pid).stream())
                .map(ClientProject::clientId)
                .collect(Collectors.toSet());
        return clients.findAll().stream().filter(c -> clientIds.contains(c.id())).collect(Collectors.toList());
    }
}