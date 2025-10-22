package edu.miu.eems.repo.jdbc;

import java.sql.*;
import java.time.*;

public final class JdbcUtil {
    private JdbcUtil() {}

    public static LocalDate getLocalDate(ResultSet rs, String col) throws SQLException {
        Date d = rs.getDate(col);
        return d == null ? null : d.toLocalDate();
    }
}