package com.moksem.moksembank.model.service;

import com.moksem.moksembank.model.entity.Card;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.repo.CardRepo;
import com.moksem.moksembank.util.exceptions.InvalidCardException;
import com.moksem.moksembank.util.exceptions.TransactionException;
import com.moksem.moksembank.util.exceptions.UserCardNotFoundException;
import com.moksem.moksembank.util.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Random;

import static com.moksem.moksembank.util.Pagination.getPage;

/**
 * Card service
 */
public class CardService {
    private final CardRepo cardRepo;
    private final UserService userService;
    private static final Random random = new Random();

    public CardService(CardRepo cardRepo, UserService userService) {
        this.cardRepo = cardRepo;
        this.userService = userService;
    }

    public List<Card> findByBalance(long id, String page, String sort) {
        int pageValue = getPage(page);

        if (sort.equals("asc"))
            return cardRepo.getCardsByWalletASC(id, pageValue);

        if (sort.equals("desc"))
            return cardRepo.getCardsByWalletDESC(id, pageValue);

        return cardRepo.getCards(id, pageValue);
    }

    public List<Card> findByParameters(long id, String page, String sort, String number) throws InvalidCardException {
        int pageValue = getPage(page);

        if (sort.equals("card")) {
            Card card = cardRepo.getCard(id, number);
            if (card == null)
                throw new InvalidCardException("client.error.card_not_found");
            return List.of(card);
        }

        if (sort.equals("request"))
            return cardRepo.getCardsByRequest(id, pageValue);

        if (sort.equals("blocked"))
            return cardRepo.getBlockedCards(id, pageValue);

        if (sort.equals("unlocked"))
            return cardRepo.getUnlockedCards(id, pageValue);

        return cardRepo.getCards(id, pageValue);
    }

    public int findCount(long id) {
        return cardRepo.getCardsCount(id);
    }

    public int findCount(long id, String sort) {
        if (sort.equals("request"))
            return cardRepo.getCardsWithRequestCount(id);

        if (sort.equals("blocked"))
            return cardRepo.getBlockedCardsCount(id);

        if (sort.equals("unlocked"))
            return cardRepo.getUnlockedCardsCount(id);

        if (sort.equals("card"))
            return 0;

        return cardRepo.getCardsCount(id);
    }

    public List<Card> findAllByUserId(long id) {
        return cardRepo.getCards(id);
    }

    public Card findByUserIdAndCardNumber(long id, String number) throws UserNotFoundException, UserCardNotFoundException {
        Card card = cardRepo.getCard(id, number);
        if (card == null)
            throw new UserCardNotFoundException("client.error.card.not_found");

        toFullCard(card);
        return card;
    }

    public Card findByNumber(String number) throws InvalidCardException, UserNotFoundException {
        Card card = cardRepo.getCard(number);
        if (card == null)
            throw new InvalidCardException("client.error.card.not_found");

        toFullCard(card);
        return card;
    }


    public Card findById(String cardId) throws InvalidCardException, UserNotFoundException {
        if(cardId == null || !cardId.matches("^\\d+$"))
            throw new InvalidCardException("client.error.card.not_found");

        Card card = cardRepo.getCard(Long.parseLong(cardId));
        if (card == null)
            throw new InvalidCardException("client.error.card.not_found");
        toFullCard(card);
        return card;
    }

    public Card findById(String cardId, User user) throws InvalidCardException, UserNotFoundException {
        if(cardId == null || !cardId.matches("^\\d+$"))
            throw new InvalidCardException("client.error.card.not_found");

        Card card = cardRepo.getCard(Long.parseLong(cardId));
        if (card == null)
            throw new InvalidCardException("client.error.card.not_found");
        toFullCard(card);
        if(!card.getUser().equals(user)) {
            throw new InvalidCardException("client.error.card.not_found");
        }
        return card;
    }

    public void create(Card card) {
        card.setNumber(getRandomCardNumber());
        if (cardRepo.getCard(card.getNumber()) != null)
            create(card);
        cardRepo.addCard(card);
    }

    public void update(Card firstCard, Card secondCard) throws TransactionException {
        cardRepo.updateCards(firstCard, secondCard);
    }

    public void update(Card card) {
        cardRepo.updateCard(card);
    }

    public void delete(String number) {
        cardRepo.deleteCard(number);
    }

    public void toFullCard(Card card) throws UserNotFoundException {
        User user = card.getUser();
        card.setUser(userService.findById(String.valueOf(user.getId())));
    }

    public static String getRandomCardNumber() {
        String[] numbers = "0123456789".split("");
        StringBuilder builder = new StringBuilder("4");
        for (int i = 0; i < 15; i++)
            builder.append(numbers[random.nextInt(numbers.length)]);
        return builder.toString();
    }


}
