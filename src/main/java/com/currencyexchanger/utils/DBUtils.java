package com.currencyexchanger.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

    public static Connection getConnect() {
        Connection connection = null;
        String url = null;
        String user = null;
        String password = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ClassLoader loader = DBUtils.class.getClassLoader();
        Properties properties = new Properties();

        try (InputStream in = loader.getResourceAsStream("database.properties")) {
            properties.load(in);
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
