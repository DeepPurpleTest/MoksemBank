package com.moksem.moksembank.model.repo;

import com.moksem.moksembank.model.entityBuilder.QueryBuilder;
import com.moksem.moksembank.model.entityBuilder.RequestQueryBuilder;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.util.exceptions.TransactionException;

import java.util.List;

public class RequestRepo {

    private static final String CREATE = "insert into request(card_number, user_id) values(?, ?)";
//    private static final String GET_ALL = "select * from request where status = ?";
    private static final String GET_BY_CARD = "select * from request where card_number = ? and status = ?";
    private static final String GET_BY_USER_ID = "select * from request where user_id = ? and status = ?";
    private static final String GET_BY_ID = "select * from request where request_id = ?";
    private static final String UPDATE = "update request set status = ?, admin_id = ? where request_id = ?";
    private static final String UPDATE_CARD = "update card set status = ?, wallet = ? where card_number = ?";

    private final QueryBuilder<Request> queryBuilder = new RequestQueryBuilder();

    public long addRequest(Card card){
        return queryBuilder.executeQueryAutoIncrement(CREATE, card.getNumber(), card.getUser().getId());
    }

    public Request getRequest(Card card) {
        return queryBuilder.executeAndReturnValue(GET_BY_CARD, card.getNumber(), false);
    }

    public Request getRequest(long id) {
        return queryBuilder.executeAndReturnValue(GET_BY_ID, id);
    }

//    public List<Request> getRequests(){
//        return queryBuilder.executeAndReturnValues(GET_ALL, false);
//    }

    public List<Request> getRequests(User user){
        return queryBuilder.executeAndReturnValues(GET_BY_USER_ID, user.getId(), false);
    }

    public void updateDoubleTransaction(Request request, Card card) throws TransactionException {
        Object[] first = {request.isStatus(), request.getAdminId(), request.getId()};
        Object[] second = {card.isStatus(), card.getWallet(), card.getNumber()};
        try {
            queryBuilder.executeDoubleTransaction(UPDATE, UPDATE_CARD, first, second);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new TransactionException();
        }

    }

    public void updateRequest(Request request){
        queryBuilder.execute(UPDATE, request.isStatus(), request.getAdminId(), request.getId());
    }
}
