package com.moksem.moksembank.model.repo;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.Payment;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.entitybuilder.PaymentQueryBuilder;
import com.moksem.moksembank.model.entitybuilder.QueryBuilder;

import java.util.List;

import static com.moksem.moksembank.util.PaginationUtil.RECORDS_PER_PAGE;

public class PaymentRepo {

    //todo если убрать все выборочные поля, а оставить дистинкт будет работать?
    private static final String GET_BY_INDEX_ASC = "select distinct card_sender, card_receiver, amount, p.status, date, payment_id " +
            "from payment p left outer join moksem_bank.card c on c.card_number = p.card_sender " +
            "or c.card_number = p.card_receiver where user_id = ? and p.status = 'sent' order by date limit ?, ?";

    private static final String GET_BY_INDEX_DESC = "select distinct card_sender, card_receiver, amount, p.status, date, payment_id " +
            "from payment p left outer join moksem_bank.card c on c.card_number = p.card_sender " +
            "or c.card_number = p.card_receiver where user_id = ? and p.status = 'sent' order by date desc limit ?, ?";
    private static final String GET_BY_CARD_INDEX_ASC = "select * from payment " +
            "where (card_sender = ? or card_receiver = ?) and status = 'sent' order by date limit ?, ?";
    private static final String GET_BY_CARD_INDEX_DESC = "select * from payment " +
            "where (card_sender = ? or card_receiver = ?) and status = 'sent' order by date desc limit ?, ?";
    private static final String GET_COUNT_BY_USER = "select count(*) from (select payment_id " +
            "from payment p left join card c " +
            "on c.card_number = p.card_sender or c.card_number = p.card_receiver " +
            "where user_id = ? and p.status = 'sent' group by payment_id) " +
            "as user_payments";
    private static final String GET_COUNT_BY_CARD = "select count(*) from (select payment_id " +
            "from payment p where (card_sender = ? or card_receiver = ?) and p.status = 'sent' " +
            "group by payment_id) as card_payments";
    private static final String GET = "select * from payment left join card c on " +
            "payment.card_sender = c.card_number or payment.card_receiver = c.card_number " +
            "where payment_id = ? and user_id = ?";
    private static final String CREATE = "insert into payment(card_sender, card_receiver, amount) values (?, ?, ?)";
    private static final String UPDATE = "update payment set status = ? where payment_id = ?";
    private static final String DELETE = "delete from payment where payment_id = ?";

    QueryBuilder<Payment> queryBuilder = new PaymentQueryBuilder();
    private static final int ROW_COUNT = RECORDS_PER_PAGE;

    public List<Payment> getPaymentsByUserASC(User user, int page) {
        return queryBuilder.executeAndReturnValues(GET_BY_INDEX_ASC, user.getId(), page * ROW_COUNT, ROW_COUNT);
    }

    public List<Payment> getPaymentsByUserDESC(User user, int page) {
        return queryBuilder.executeAndReturnValues(GET_BY_INDEX_DESC, user.getId(), page * ROW_COUNT, ROW_COUNT);
    }

    public List<Payment> getPaymentsByCardASC(Card card, int page) {
        return queryBuilder.executeAndReturnValues(GET_BY_CARD_INDEX_ASC, card.getNumber(),
                card.getNumber(), page * ROW_COUNT, ROW_COUNT);
    }

    public List<Payment> getPaymentsByCardDESC(Card card, int page) {
        return queryBuilder.executeAndReturnValues(GET_BY_CARD_INDEX_DESC, card.getNumber(),
                card.getNumber(), page * ROW_COUNT, ROW_COUNT);
    }

    public long createPayment(Payment payment) {
        return queryBuilder.executeQueryAutoIncrement(CREATE, payment.getCardSender().getNumber(), payment.getCardReceiver().getNumber(), payment.getAmount());
    }

    public int getCountByUser(User user) {
        return queryBuilder.executeAndReturnCount(GET_COUNT_BY_USER, user.getId());
    }

    public int getCountByCard(String card) {
        return queryBuilder.executeAndReturnCount(GET_COUNT_BY_CARD, card, card);
    }

    public Payment getPayment(long userId, int paymentId) {
        return queryBuilder.executeAndReturnValue(GET, paymentId, userId);
    }

    public void update(Payment payment) {
        queryBuilder.execute(UPDATE, payment.getStatus(), payment.getId());
    }

    public void delete(Payment payment) {
        queryBuilder.execute(DELETE, payment.getId());
    }
}
