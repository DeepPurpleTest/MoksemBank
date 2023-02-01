package com.moksem.moksembank.model.repo;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entityBuilder.CardQueryBuilder;
import com.moksem.moksembank.model.entityBuilder.QueryBuilder;
import com.moksem.moksembank.util.exceptions.TransactionException;

import java.util.List;

import static com.moksem.moksembank.util.PaginationUtil.RECORDS_PER_PAGE;


public class CardRepo {
    private static final String GET_ALL_BY_USER = "select * from card where user_id = ?";
    private static final String GET_BY_WALLET = "select * from card where user_id = ? limit ?, ?";
    private static final String GET_BY_WALLET_ASC = "select * from card where user_id = ? order by wallet limit ?, ?";
    private static final String GET_BY_WALLET_DESC = "select * from card where user_id = ? order by wallet desc limit ?, ?";
    private static final String GET_COUNT_BY_USER = "select count(card_number) from card where user_id = ?";
    private static final String GET_BY_NUMBER = "select * from card where card_number = ?";
    private static final String GET_BY_ID = "select * from card where card_id = ?";
    private static final String GET_BY_NUMBER_USER = "select * from card where card_number = ? and user_id = ?";
    private static final String GET_BY_CARD_NUMBER_USER_ID = "select * from card where card_number = ? and user_id = ?";
    private static final String GET_BY_REQUEST = "select c.card_number, c.user_id, wallet, c.status, card_id " +
            "from ((select * from card where user_id = ?) as c " +
            "left join request r on c.card_number = r.card_number) " +
            "where c.status = false and r.status = false limit ?, ?";
    private static final String GET_BLOCKED = "select * from card where status = false and user_id = ? limit ?, ?";
    private static final String GET_UNLOCKED = "select * from card where status = true and user_id = ? limit ?, ?";
    private static final String GET_CARDS_WITH_REQUEST_COUNT = "select count(*) " +
            "from ((select * from card where user_id = ?) as c " +
            "left join request r on c.card_number = r.card_number) " +
            "where c.status = false and r.status = false";
    private static final String GET_BLOCKED_COUNT = "select count(*) from card where status = false and user_id = ?";
    private static final String GET_UNLOCKED_COUNT = "select count(*) from card where status = true and user_id = ?";
    private static final String CREATE = "insert into card(user_id, card_number) values (?, ?)";
    private static final String DELETE = "delete from card where card_number = ?";
    private static final String UPDATE = "update card set status = ?, wallet = ? where card_number = ?";


    QueryBuilder<Card> queryBuilder = new CardQueryBuilder();

    private static final int ROW_COUNT = RECORDS_PER_PAGE;



    public List<Card> getCards(long id, int page){
        return queryBuilder.executeAndReturnValues(GET_BY_WALLET, id, page* ROW_COUNT, ROW_COUNT);
    }

    public List<Card> getCardsByWalletASC(long id, int page){
            return queryBuilder.executeAndReturnValues(GET_BY_WALLET_ASC, id, page* ROW_COUNT, ROW_COUNT);
    }

    public List<Card> getCardsByWalletDESC(long id, int page){
        return queryBuilder.executeAndReturnValues(GET_BY_WALLET_DESC, id, page* ROW_COUNT, ROW_COUNT);
    }

    public List<Card> getCards(long id){
        return queryBuilder.executeAndReturnValues(GET_ALL_BY_USER, id);
    }

    public List<Card> getCards(long id, String number){
        return queryBuilder.executeAndReturnValues(GET_BY_NUMBER_USER, number, id);
    }

    public int getCardsCount(long id){
        return queryBuilder.executeAndReturnCount(GET_COUNT_BY_USER, id);
    }

    public Card getCard(String cardNumber){
        return queryBuilder.executeAndReturnValue(GET_BY_NUMBER, cardNumber);
    }

    public Card getCard(long id, String number){
        return queryBuilder.executeAndReturnValue(GET_BY_CARD_NUMBER_USER_ID, number, id);
    }

    public Card getCard(long id){
        return queryBuilder.executeAndReturnValue(GET_BY_ID, id);
    }

    public void addCard(Card card){
        queryBuilder.execute(CREATE, card.getUser().getId(), card.getNumber());
    }

    public void deleteCard(String number){
        queryBuilder.execute(DELETE, number);
    }

    public void updateCards(Card firstCard, Card secondCard) throws TransactionException {
        Object[] first = {firstCard.isStatus(), firstCard.getWallet(), firstCard.getNumber()};
        Object[] second = {secondCard.isStatus(), secondCard.getWallet(), secondCard.getNumber()};
        queryBuilder.executeDoubleTransaction(UPDATE, UPDATE, first, second);
    }

    public void updateCard(Card card){
        queryBuilder.execute(UPDATE, card.isStatus(), card.getWallet(), card.getNumber());
    }

    public List<Card> getCardsByRequest(long id, int page) {
        return queryBuilder.executeAndReturnValues(GET_BY_REQUEST, id, page* ROW_COUNT, ROW_COUNT);
    }

    public List<Card> getBlockedCards(long id, int page) {
        return queryBuilder.executeAndReturnValues(GET_BLOCKED, id, page* ROW_COUNT, ROW_COUNT);
    }

    public List<Card> getUnlockedCards(long id, int page) {
        return queryBuilder.executeAndReturnValues(GET_UNLOCKED, id, page* ROW_COUNT, ROW_COUNT);
    }

    public int getCardsWithRequestCount(long id) {
        return queryBuilder.executeAndReturnCount(GET_CARDS_WITH_REQUEST_COUNT, id);
    }

    public int getBlockedCardsCount(long id) {
        return queryBuilder.executeAndReturnCount(GET_BLOCKED_COUNT, id);
    }

    public int getUnlockedCardsCount(long id) {
        return queryBuilder.executeAndReturnCount(GET_UNLOCKED_COUNT, id);
    }

//    public List<Card> find(int id, String page, String sort, String card) throws InvalidIdException {
//        ValidatorsUtil.validateId(id);
//        int pageValue = PaginationUtil.getPage(page);
//
//        if(sort == null || sort.isEmpty())
//            return getCards(id, pageValue);
//
//        if (sort.equals("card"))
//            return getCards(id, card);
//
//        if(sort.equals("request"))
//            return getCardsByRequest(id, pageValue);
//
//        if(sort.equals("blocked"))
//            return getBlockedCards(id, pageValue);
//
//        if(sort.equals("unlocked"))
//            return getUnlockedCards(id,pageValue);
//
//        return getCards(id, pageValue);
//    }
}
