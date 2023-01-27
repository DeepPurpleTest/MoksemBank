package com.moksem.moksembank.model.entityBuilder;

import com.moksem.moksembank.model.connection.DataSource;

import java.sql.*;
import java.util.List;

public abstract class QueryBuilder<T> {

    public void execute(String query, Object... args){
        executeQuery(query, args);
    }


    //todo почекать как делать rollback нормально
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

    public void executeDoubleTransaction(String query1, String query2, Object[] args1, Object[] args2) throws RuntimeException{
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
            throw new RuntimeException(e);
        } finally {
            DataSource.closeConnection(connection);
        }
    }

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

    public void setPreparedStatement(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++)
            ps.setObject(i + 1, args[i]);
    }
    public abstract List<T> getListOfResult(ResultSet executeQuery) throws SQLException;
    public abstract T getResult(ResultSet rs) throws SQLException;
}
