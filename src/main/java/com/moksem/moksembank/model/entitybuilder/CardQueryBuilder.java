package com.moksem.moksembank.model.entitybuilder;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;

import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardQueryBuilder extends QueryBuilder<Card> {

    @Override
    public List<Card> getListOfResult(ResultSet rs) throws SQLException {
        List<Card> cards = new ArrayList<>();
        while (rs.next()){
            User cardUser = User.builder().build();
            cardUser.setId(rs.getLong(2));
            Card card = Card.builder()
                    .number(rs.getString(1))
                    .user(cardUser)
                    .wallet(rs.getBigDecimal(3).setScale(2, RoundingMode.DOWN))
                    .status(rs.getBoolean(4))
                    .build();
            card.setId(rs.getLong(5));
            cards.add(card);
        }
        return cards;
    }

    @Override
    public Card getResult(ResultSet rs) throws SQLException {
        Card card = null;
        while (rs.next()){
            User cardUser = User.builder().build();
            cardUser.setId(rs.getLong(2));
            card = Card.builder()
                    .number(rs.getString(1))
                    .user(cardUser)
                    .wallet(rs.getBigDecimal(3).setScale(2, RoundingMode.DOWN))
                    .status(rs.getBoolean(4))
                    .build();
            card.setId(rs.getLong(5));
        }
        return card;
    }
}
