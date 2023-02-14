package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Request;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.RequestRepo;
import com.moksem.moksembank.util.exceptions.TransactionException;

import java.util.List;

/**
 * Request service
 */
public class RequestService {
    private final RequestRepo requestRepo;

    public RequestService(RequestRepo requestRepo) {
        this.requestRepo = requestRepo;
    }

    public Request findByCard(Card card){
        return requestRepo.getRequest(card);
    }

    public Request findByUserId(long id){
        return requestRepo.getRequest(id);
    }

    public List<Request> findAllByUser(User user){
        return requestRepo.getRequests(user);
    }

    public long create(Card card){
        return requestRepo.addRequest(card);
    }

    public void update(Request request){
        requestRepo.updateRequest(request);
    }

    public void updateRequestTransaction(Request request, Card card) throws TransactionException {
        requestRepo.updateDoubleTransaction(request, card);
    }

}
