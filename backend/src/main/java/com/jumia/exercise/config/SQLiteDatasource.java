package com.jumia.exercise.config;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.sqlite.Function;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class SQLiteDatasource extends SimpleDriverDataSource {

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = super.getConnection();
        Function.create(connection, "REGEXP", new Function() {
            @Override
            protected void xFunc() throws SQLException {
                String value = value_text(0);
                String expression = value_text(1);
                if (value == null)
                    value = "";
                boolean matches = Pattern.matches(expression, value);
                result(matches ? 1 : 0);
            }
        });
        return connection;
    }
}