package edu.miu.eems.dao.client;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.dao.support.RowMappers;
import edu.miu.eems.domain.Client;
import java.sql.*;
import java.util.*;

public class FindAllClientsDao implements Dao<Client> {
    private final List<Client> out = new ArrayList<>();

    @Override
    public String getSql(){
        return "SELECT * FROM client ORDER BY id";
    }

    @Override
    public void unpack(ResultSet rs) throws SQLException {
        while(rs.next())
            out.add(RowMappers.client(rs));
    }

    @Override public List<Client> getResults(){
        return out;
    }
}
