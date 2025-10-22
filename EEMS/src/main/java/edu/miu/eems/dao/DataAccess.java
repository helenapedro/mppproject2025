package edu.miu.eems.dao;

import java.sql.*;

public interface DataAccess {
    <T> void read(Dao<T> dao) throws SQLException;
    <T> void write(Dao<T> dao) throws SQLException;
    void inTransaction(SQLWork work) throws SQLException;

    @FunctionalInterface
    interface SQLWork {
        void accept(Connection con) throws SQLException;
    }
}
