package com.moksem.moksembank.model.entitybuilder;

import com.moksem.moksembank.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity query builder
 */
public class UserQueryBuilder extends QueryBuilder<User> {
    public List<User> getListOfResult(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()){
            User user = User.builder()
                    .name(rs.getString(2))
                    .surname(rs.getString(3))
                    .middleName(rs.getString(4))
                    .password(rs.getString(5))
                    .phoneNumber(rs.getString(6))
                    .status(rs.getBoolean(7))
                    .role(rs.getInt(8))
                    .build();
            user.setId(rs.getInt(1));
            users.add(user);
        }
        return users;
    }

    public User getResult(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()){
            user = User.builder()
                    .name(rs.getString(2))
                    .surname(rs.getString(3))
                    .middleName(rs.getString(4))
                    .password(rs.getString(5))
                    .phoneNumber(rs.getString(6))
                    .status(rs.getBoolean(7))
                    .role(rs.getInt(8))
                    .build();
            user.setId(rs.getInt(1));
        }
        return user;
    }
}
