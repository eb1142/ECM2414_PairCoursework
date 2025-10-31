package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    static int idCounter = 0;
    private final int playerNum;
    private final ArrayList<Card> hand;
    private Deck discardDeck;
    private Deck drawDeck;

    public Player() {
        playerNum = ++idCounter;
        hand = new ArrayList<>();
    }
    public void setDrawDeck(Deck deck) {
        drawDeck = deck;
    }
    public void setDiscardDeck(Deck deck) {
        discardDeck = deck;
    }

    public int getID() {
        return playerNum;
    }

    // for testing
    public String handToString() {
        String result = "";
        for (Card card : hand) {
            result = result.concat("|" + Integer.toString(card.getValue()));
        }
        return result;
    }
    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }
    //adds card to hand from head of deck
    public void drawCard() {
        Card card = drawDeck.drawCard();
        addCard(card);
        addToOutput(String.format("player %d draws a %d from deck %d", playerNum, card.getValue(), drawDeck.getID()));
    }

    //adds a card to bottom of deck and removes from hand
    public void discardCard(Card card) {
        removeCard(card);
        discardDeck.addCard(card);
        addToOutput(String.format("player %d discards a %d to deck %d", playerNum, card.getValue(), discardDeck.getID()));
    }

    //picks card that doesn't equal the player number
    public Card pickCard() {
        for (Card card : hand) {
            if (card.getValue() != playerNum) {
                return card;
            }
        }
        /*this is where there are 5 cards that are all right
        , doesn't matter which is picked*/
        return hand.get(0);
    }

    public boolean checkWon() {
        //checks if every card is the same value in hand
        int current = hand.get(0).getValue();
        for (Card card : hand) {
            if (card.getValue() != current)
                return false;
        }
        return true;
    }

    //a turn of play for a player
    /*at the moment, there are two decks as arguments
    , it may be better to implement these as attributes instead*/
    public void turn() {
        if (checkWon()) {
            //win statement
        }
        else {
            drawCard();
            Card card = pickCard();
            discardCard(card);
            addToOutput(String.format("player %d current hand is %d %d %d %d", playerNum, hand.get(0), hand.get(1), hand.get(2), hand.get(3)));
        }
    }

    public void addToOutput(String text) {
        try (FileWriter writer = new FileWriter(String.format("player%d_output.txt", playerNum), true)) {
            writer.write(text + "\n");
        } catch (IOException e) {
            System.err.println("Error when writing to player text file");
        }
    }
}