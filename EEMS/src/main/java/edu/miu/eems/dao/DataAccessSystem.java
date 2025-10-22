package edu.miu.eems.dao;

import edu.miu.eems.db.DB;

import java.sql.*;

/** Minimal implementation that delegates connections to DB.getConnection() */
public final class DataAccessSystem implements DataAccess {
    @Override
    public <T> void read(Dao<T> dao) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(dao.getSql())) {
            dao.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                dao.unpack(rs);
            }
        }
    }

    @Override
    public <T> void write(Dao<T> dao) throws SQLException {
        int flags = dao.wantsGeneratedKeys() ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(dao.getSql(), flags)) {
            dao.bind(ps);
            ps.executeUpdate();
            if (dao.wantsGeneratedKeys())
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    dao.handleGeneratedKeys(keys);
                }
        }
    }
    @Override
    public void inTransaction(SQLWork work) throws SQLException {
        try (Connection con = DB.getConnection()) {
            boolean old = con.getAutoCommit();
            con.setAutoCommit(false);
            try {
                work.accept(con); con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
            finally {
                con.setAutoCommit(old);
            }
        }
    }
}