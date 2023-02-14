package com.moksem.moksembank.model.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Connection pool for DB
 */
public class DataSource {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    //Create a pool
    static {
        Properties properties = getProperties();
        config.setJdbcUrl(properties.getProperty("JDBC_URL"));
        config.setUsername(properties.getProperty("USER_NAME"));
        config.setPassword(properties.getProperty("PASSWORD"));
        config.setDriverClassName(properties.getProperty("DRIVER"));
        config.addDataSourceProperty("cachePrepStmts", properties.getProperty("CACHE_PREPARED_STATEMENT"));
        config.addDataSourceProperty("prepStmtCacheSize", properties.getProperty("CACHE_PREPARED_STATEMENT_SIZE"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", properties.getProperty("CACHE_PREPARED_STATEMENT_SQL_LIMIT"));
        ds = new HikariDataSource(config);
    }

    private DataSource() {
    }

    /**
     * Establishes a connection to the database
     */
    public static synchronized Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets utils properties
     */
    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream resource = DataSource.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Close connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}