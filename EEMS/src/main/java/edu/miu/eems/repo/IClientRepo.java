package edu.miu.eems.repo;

import edu.miu.eems.domain.Client;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IClientRepo extends ICrud<Client,Integer> {


    List<Client> findClientsWithProjectsEndingBy(LocalDate endDate);
}