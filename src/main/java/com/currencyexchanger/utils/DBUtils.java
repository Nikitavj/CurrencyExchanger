package com.currencyexchanger.utils;

import com.currencyexchanger.controller.exception.DatabaseException;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DBUtils {

    public static Optional<Connection> getConnect() {

        ClassLoader loader = DBUtils.class.getClassLoader();

        try {
            Class.forName("org.sqlite.JDBC");
            URL res = loader.getResource("exchanger.db");
            File file = new File(res.getFile());
            String dbPath = "jdbc:sqlite://" + file.getAbsolutePath();

            Connection connection = DriverManager.getConnection(dbPath);
            return Optional.of(connection);

        } catch (SQLException | ClassNotFoundException e) {
            new DatabaseException();
        }
        return Optional.empty();
    }
}
