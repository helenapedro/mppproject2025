package edu.miu.eems.dao.client;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.Client;
import java.sql.*;
import java.util.*;

public class UpsertClientDao implements Dao<Integer> {
    private final Client c;

    public UpsertClientDao(Client c){
        this.c = c;
    }

    @Override
    public String getSql(){
        return "INSERT INTO client(id,name,industry,email,phone) VALUES(?,?,?,?,?) " +
            "ON DUPLICATE KEY UPDATE name=VALUES(name), industry=VALUES(industry), email=VALUES(email), phone=VALUES(phone)";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,c.id());
        ps.setString(2,c.name());
        ps.setString(3,c.industry());
        ps.setString(4,c.email());
        ps.setString(5,c.phone());
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}
