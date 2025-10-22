package edu.miu.eems.dao.client;

import edu.miu.eems.dao.Dao; import edu.miu.eems.dao.support.RowMappers; import edu.miu.eems.domain.Client;
import java.sql.*; import java.util.*;

public class FindClientByIdDao implements Dao<Client> {
    private final int id;
    private final List<Client> out = new ArrayList<>();

    public FindClientByIdDao(int id){
        this.id = id;
    }

    @Override public String getSql(){
        return "SELECT * FROM client WHERE id=?";
    }

    @Override
    public void bind(PreparedStatement ps) throws SQLException {
        ps.setInt(1,id);
    }

    @Override public void unpack(ResultSet rs) throws SQLException {
        if(rs.next())
            out.add(RowMappers.client(rs));
    }

    @Override public List<Client> getResults(){
        return out;
    }
}
