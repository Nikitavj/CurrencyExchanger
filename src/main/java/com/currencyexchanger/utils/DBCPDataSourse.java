package com.currencyexchanger.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBCPDataSourse {

    private static String NAME_PROPERTIES = "configdb.properties";
    private static BasicDataSource ds = new BasicDataSource();

    static {
        ClassLoader loader = DBCPDataSourse.class.getClassLoader();

        try {
            Properties config = new Properties();
            InputStream is = loader.getResourceAsStream(NAME_PROPERTIES);
            config.load(is);

            String dbName = config.getProperty("db_name");
            String dbDriver = config.getProperty("db_driver");
            String dbUrl = config.getProperty("db_url");
            String userName = config.getProperty("userName");
            String password = config.getProperty("password");

            URL res = loader.getResource(dbName);
            String path = new File(res.toURI()).getAbsolutePath();
            String dbPath = dbUrl + path;

            ds.setDriverClassName(dbDriver);
            ds.setUrl(dbPath);
            ds.setUsername(userName);
            ds.setPassword(password);
            ds.setMinIdle(5);
            ds.setMinIdle(10);
            ds.setMaxOpenPreparedStatements(100);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private DBCPDataSourse() { };

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
