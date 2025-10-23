package edu.miu.eems.dao.client;

import edu.miu.eems.dao.Dao;
import edu.miu.eems.domain.Client;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * A DAO (Data Access Object) class that handles the SQL
 * and parameter binding for a standard UPDATE operation on a Client.
 */
public class UpdateClientDao implements Dao<Integer> {
    private final Client c;

    public UpdateClientDao(Client c) {
        this.c = c;
    }
    @Override
    public String getSql() {
        return "UPDATE client SET name = ?, industry = ?, email = ?, phone = ? " +
                "WHERE id = ?";
    }


    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        // Parameters for the SET clause
        ps.setString(1, c.name());
        ps.setString(2, c.industry());
        ps.setString(3, c.email());
        ps.setString(4, c.phone());

        // Parameter for the WHERE clause
        ps.setInt(5, c.id());
    }


    @Override
    public List<Integer> getResults() {
        return List.of();
    }
}