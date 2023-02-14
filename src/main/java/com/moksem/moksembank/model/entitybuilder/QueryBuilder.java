package com.moksem.moksembank.model.entitybuilder;

import com.moksem.moksembank.model.connection.DataSource;
import com.moksem.moksembank.util.exceptions.TransactionException;

import java.sql.*;
import java.util.List;

/**
 * Abstract class for builders.
 */
public abstract class QueryBuilder<T> {

    /**
     * Execute query (CREATE, UPDATE, DELETE) the database
     *
     * @param query the database query
     * @param args  the args
     */
    public void execute(String query, Object... args){
        executeQuery(query, args);
    }


    /**
     * Execute query (CREATE, UPDATE, DELETE) the database
     *
     * @param query the database query
     * @param args  the args
     */
    public void executeQuery(String query, Object... args) {
        Connection connection = DataSource.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)){
            setPreparedStatement(ps, args);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection(connection);
        }
    }

    /**
     * Return generated autoincrement
     *
     * @param query the database query
     * @param args  the args
     * @return      generated autoincrement
     */
    public long executeQueryAutoIncrement(String query, Object... args){
        long id = -1;
        Connection connection = DataSource.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
            setPreparedStatement(ps, args);
            connection.setAutoCommit(false);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()){
                while (rs.next())
                    id = rs.getLong(1);
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection(connection);
        }
        return id;
    }

    /**
     * Execute query with rollback
     *
     * @param query1    the first database query
     * @param query2    the second database query
     * @param args1     args for first query
     * @param args2     args for second query
     * @throws TransactionException Transaction exception
     */
    public void executeDoubleTransaction(String query1, String query2, Object[] args1, Object[] args2) throws TransactionException {
        Connection connection = DataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(query1);
            setPreparedStatement(ps, args1);
            ps.executeUpdate();
            ps = connection.prepareStatement(query2);
            setPreparedStatement(ps, args2);
            ps.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransactionException("Transaction is failed");
        } finally {
            DataSource.closeConnection(connection);
        }
    }

    /**
     * QueryBuilder for methods with returned list of entities
     *
     * @param query the database query
     * @param args  the args
     * @return the list of {@link com.moksem.moksembank.model.entity}
     */
    public List<T> executeAndReturnValues(String query, Object... args) {
        List<T> models = null;
        Connection connection = DataSource.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)){
            setPreparedStatement(ps, args);
            try (ResultSet rs = ps.executeQuery()){
                models = getListOfResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection(connection);
        }
        return models;
    }

    /**
     * QueryBuilder for methods with returned one entity
     *
     * @param query the database query
     * @param args  the args
     * @return object of {@link com.moksem.moksembank.model.entity.Entity}
     */
    public T executeAndReturnValue(String query, Object... args) {
        T model = null;
        Connection connection = DataSource.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)){
            setPreparedStatement(ps, args);
            try (ResultSet rs = ps.executeQuery()){
                model = getResult(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection(connection);
        }
        return model;
    }

    /**
     * QueryBuilder for methods with number of records
     *
     * @param query the database query
     * @param args  the args
     * @return {@link Integer}
     */
    public Integer executeAndReturnCount(String query, Object... args){
        int count = 0;
        Connection connection = DataSource.getConnection();
        try (PreparedStatement ps = connection.prepareStatement(query)){
            setPreparedStatement(ps, args);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next())
                    count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection(connection);
        }
        return count;
    }

    /**
     * Sets args of prepared statement
     *
     * @param ps    the args
     * @param args  the PreparedStatement
     * @throws SQLException SQL exception
     */
    public void setPreparedStatement(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++)
            ps.setObject(i + 1, args[i]);
    }

    /**
     * Creates list of objects
     *
     * @param rs    the ResultSet
     * @return      the list of {@link com.moksem.moksembank.model.entity.Entity}
     * @throws      SQLException SQL exception
     */
    public abstract List<T> getListOfResult(ResultSet rs) throws SQLException;

    /**
     * Creates object
     *
     * @param rs    the ResultSet
     * @return      object of {@link com.moksem.moksembank.model.entity.Entity}
     * @throws      SQLException SQL exception
     */
    public abstract T getResult(ResultSet rs) throws SQLException;
}
