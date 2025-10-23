package edu.miu.eems.repo;

import edu.miu.eems.domain.Client;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// This interface should extend ICrud
public interface IClientRepo extends ICrud<Client,Integer> {

    // --- ADD THIS LINE ---
    List<Client> findClientsWithProjectsEndingBy(LocalDate endDate);
}