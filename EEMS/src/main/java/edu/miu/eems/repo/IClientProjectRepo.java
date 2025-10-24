package edu.miu.eems.repo;

import edu.miu.eems.domain.ClientProject;
import java.util.List;

public interface IClientProjectRepo {
    ClientProject add(ClientProject cp);
    List<ClientProject> findByClient(int clientId);
    List<ClientProject> findByProject(int projectId);
    boolean delete(int clientId, int projectId);
}