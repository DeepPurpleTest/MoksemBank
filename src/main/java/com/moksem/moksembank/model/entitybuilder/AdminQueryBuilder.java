package com.moksem.moksembank.model.entitybuilder;

import com.moksem.moksembank.model.entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin entity query builder
 */
public class AdminQueryBuilder extends QueryBuilder<Admin>{
    @Override
    public List<Admin> getListOfResult(ResultSet rs) throws SQLException {
        List<Admin> admins = new ArrayList<>();
        while(rs.next()){
            Admin admin = Admin.builder()
                    .login(rs.getString(2))
                    .password(rs.getString(3))
                    .role(rs.getInt(4))
                    .build();
            admin.setId(rs.getInt(1));
            admins.add(admin);
        }
        return admins;
    }

    @Override
    public Admin getResult(ResultSet rs) throws SQLException {
        Admin admin = null;
        while (rs.next()){
            admin = Admin.builder()
                    .login(rs.getString(2))
                    .password(rs.getString(3))
                    .role(rs.getInt(4))
                    .build();
            admin.setId(rs.getInt(1));
        }
        return admin;
    }
}
