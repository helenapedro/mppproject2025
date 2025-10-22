package edu.miu.eems.dao.client;

import edu.miu.eems.dao.Dao;
import java.sql.*;
import java.util.*;

public class DeleteClientByIdDao implements Dao<Integer> {
    private final int id;

    public DeleteClientByIdDao(int id){
        this.id = id;
    }

    @Override
    public String getSql(){
        return "DELETE FROM client WHERE id=?";
    }

    @Override public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,id);
    }

    @Override public List<Integer> getResults(){
        return List.of();
    }
}
