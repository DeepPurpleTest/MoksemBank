package com.moksem.moksembank.model.entityBuilder;

import com.moksem.moksembank.model.entity.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestQueryBuilder extends QueryBuilder<Request>{
    @Override
    public List<Request> getListOfResult(ResultSet rs) throws SQLException {
        List<Request> requests = new ArrayList<>();
        while(rs.next()){
            Request request = Request.builder()
                    .cardNumber(rs.getString(2))
                    .status(rs.getBoolean(3))
                    .userId(rs.getInt(4))
                    .adminId(rs.getInt(5))
                    .build();
            request.setId(rs.getInt(1));
            requests.add(request);
        }
        return requests;
    }

    @Override
    public Request getResult(ResultSet rs) throws SQLException {
        Request request = null;
        while (rs.next()){
            request = Request.builder()
                    .cardNumber(rs.getString(2))
                    .status(rs.getBoolean(3))
                    .userId(rs.getInt(4))
                    .adminId(rs.getInt(5))
                    .build();
            request.setId(rs.getInt(1));
        }
        return request;
    }

}
