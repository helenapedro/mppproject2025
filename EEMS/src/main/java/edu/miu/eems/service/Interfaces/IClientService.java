package edu.miu.eems.service.Interfaces;

import edu.miu.eems.domain.Client;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    List<Client> findClientsByUpcomingProjectDeadline(int days);
    void addClient(Client c);
    void deleteById(int cid);
    void update(Client c);
    List<Client> findAllClients();
    Optional<Client> findById(int id);
}