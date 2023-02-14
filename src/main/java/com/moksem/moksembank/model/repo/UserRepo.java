package com.moksem.moksembank.model.repo;

import com.moksem.moksembank.model.entitybuilder.QueryBuilder;
import com.moksem.moksembank.model.entitybuilder.UserQueryBuilder;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.util.Pagination;

import java.util.List;

/**
 * User repository
 */
public class UserRepo {
    private static final int ROWCOUNT = Pagination.RECORDS_PER_PAGE;
    private static final String GET_ALL = "select * from user limit ?, ?";
    private static final String GET_BY_REQUEST = "select distinct u.user_id, u.name, u.surname, u.middle_name, u.password, u.phone_number, u.status, u.role " +
            "from request r left join user u on r.user_id = u.user_id where r.status = false limit ?, ?";
    private static final String GET_BY_CARD = "select u.user_id, name, surname, middle_name, password, phone_number, u.status, u.role " +
            "from user u left join card c on c.user_id = u.user_id where c.card_number = ?";
    private static final String CREATE = "insert into user(name, surname, middle_name, password, phone_number) " +
            "values (?, ?, ?, ?, ?)";
    private static final String GET_BY_PHONE = "select * from user where phone_number = ?";
    private static final String GET_BY_PHONE_AND_ID = "select * from user where phone_number = ? and user_id != ?";
    private static final String GET_BY_ID = "select * from user where user_id = ?";
    private static final String UPDATE = "update user set name = ?, surname = ?, middle_name = ?, password = ?, " +
            "phone_number = ?, status = ? where user_id = ?";
    private static final String GET_USERS_COUNT = "select count(*) from user";
    private static final String GET_USERS_REQUEST_COUNT = "select count(*) " +
            "from (select distinct u.user_id, u.name, u.surname, u.middle_name, u.password, u.phone_number, u.status, u.role " +
            "from request r left join user u on r.user_id = u.user_id where r.status = false) as req";

    QueryBuilder<User> queryBuilder = new UserQueryBuilder();


    public List<User> getClients(int page){
        return queryBuilder.executeAndReturnValues(GET_ALL, page*ROWCOUNT, ROWCOUNT);
    }

    public List<User> getClientsByRequest(int page){
        return queryBuilder.executeAndReturnValues(GET_BY_REQUEST, page*ROWCOUNT, ROWCOUNT);
    }

    public long newUser(User user){
        return queryBuilder.executeQueryAutoIncrement(CREATE, user.getName(), user.getSurname(), user.getMiddleName(),
                user.getPassword(), user.getPhoneNumber());
    }

    public User getUser(String phoneNumber) {
        return queryBuilder.executeAndReturnValue(GET_BY_PHONE, phoneNumber);
    }

    public User getUser(int id) {
        return queryBuilder.executeAndReturnValue(GET_BY_ID, id);
    }
    public User getUserByPhoneAndId(User user){
        return queryBuilder.executeAndReturnValue(GET_BY_PHONE_AND_ID, user.getPhoneNumber(), user.getId());
    }

    public User getUserByCard(String cardNumber){
        return queryBuilder.executeAndReturnValue(GET_BY_CARD, cardNumber);
    }

    public void updateUser(User user){
        queryBuilder.execute(UPDATE, user.getName(), user.getSurname(), user.getMiddleName(),
                user.getPassword(), user.getPhoneNumber(), user.isStatus(), user.getId());
    }

    public int getUsersCount(){
        return queryBuilder.executeAndReturnCount(GET_USERS_COUNT);
    }

    public int getUsersRequestCount(){
        return queryBuilder.executeAndReturnCount(GET_USERS_REQUEST_COUNT);
    }
}
