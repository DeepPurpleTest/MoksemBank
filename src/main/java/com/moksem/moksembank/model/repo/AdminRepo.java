package com.moksem.moksembank.model.repo;

import com.moksem.moksembank.model.entityBuilder.AdminQueryBuilder;
import com.moksem.moksembank.model.entityBuilder.QueryBuilder;
import com.moksem.moksembank.model.entity.Admin;

public class AdminRepo {
    private static final String GET = "select * from admin where login = ?";
    private static final String GET_BY_LOGIN_AND_ID = "select * from admin where login = ? and admin_id != ?";
    private static final String UPDATE = "update admin set login = ?, password = ? where admin_id = ?";

    QueryBuilder<Admin> queryBuilder = new AdminQueryBuilder();

    public Admin getAdmin(String login){
        return queryBuilder.executeAndReturnValue(GET, login);
    }
    public void update(Admin admin){
        queryBuilder.execute(UPDATE, admin.getLogin(), admin.getPassword(), admin.getId());
    }

    public Admin getSameAdmin(Admin admin){
        return queryBuilder.executeAndReturnValue(GET_BY_LOGIN_AND_ID, admin.getLogin(), admin.getId());
    }
}
