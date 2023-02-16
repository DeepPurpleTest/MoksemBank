package com.moksem.moksembank.model.dtobuilder;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.model.dto.CardDto;
import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.service.RequestService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Card entity dto builder
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardDtoBuilder {
    static RequestService requestService = AppContext.getInstance().getRequestService();

    public static CardDto getCardDto(Card card) {
        CardDto cardDto = CardDto.builder()
                .number(card.getNumber() == null ? "" : card.getNumber())
                .wallet(card.getWallet() == null ? "" : card.getWallet().toString())
                .status(card.isStatus())
                .build();
        cardDto.setId(card.getId() == 0 ? "" : String.valueOf(card.getId()));

        if (requestService.findByCard(card) != null)
            cardDto.setRequest(true);

        return cardDto;
    }

    public static List<CardDto> getCardsDto(List<Card> cards) {
        List<CardDto> cardDtoList = new ArrayList<>();
        if (cards.isEmpty())
            return cardDtoList;
        cards.forEach(card -> cardDtoList.add(getCardDto(card)));
        return cardDtoList;
    }
}
