package edu.miu.eems.dao;

import java.sql.*;
import java.util.*;

/** Generic DAO contract for a single SQL statement. */
public interface Dao<T> {
    /** SQL string. Use ? placeholders for inputs. */
    String getSql();
    /** Bind parameters; default no-op for statements without params. */
    default void bind(PreparedStatement ps) throws SQLException {}
    /** For SELECT statements, map rows to results; default no-op for DML. */
    default void unpack(ResultSet rs) throws SQLException {}
    /** For INSERTs that need generated keys. */
    default boolean wantsGeneratedKeys() { return false; }
    /** Handle generated keys if wantsGeneratedKeys()==true */
    default void handleGeneratedKeys(ResultSet keys) throws SQLException {}
    /** Collected results (for SELECTs) */
    List<T> getResults();
}