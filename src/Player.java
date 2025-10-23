import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private static int idCounter = 0;
    private final int playerNum;
    private ArrayList<Card> hand;

    public Player() {
        playerNum = ++idCounter;
        hand = new ArrayList<>();
    }

    public int getID() {
        return playerNum;
    }
    
    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }
    //adds card to hand from head of deck
    public void drawCard(Deck deck) {
        Card card = deck.drawCard();
        addCard(card);
        addToOutput(String.format("player %d draws a %d from deck %d", playerNum, card.getValue(), deck.getDeckID()));
    }

    //adds a card to bottom of deck and removes from hand
    public void discardCard(Deck deck, Card card) {
        removeCard(card);
        deck.addDiscardedCard(card);
        addToOutput(String.format("player %d discards a %d to deck %d", playerNum, card.getValue(), deck.getDeckID()));
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
    public void turn(Deck discardDeck, Deck drawDeck) {
        if (checkWon()) {
            //win statement
        }
        else {
            drawCard(drawDeck);
            Card card = pickCard();
            discardCard(discardDeck, card);
            addToOutput(String.format("player %d current hand is %d %d %d %d", playerNum, hand.get(0), hand.get(1), hand.get(2), hand.get(3)));
        }
    }

    public void addToOutput(String text) {
        try (FileWriter writer = new FileWriter(String.format("player%d_output.txt", playerNum))) {
            writer.write(text + "\n");
        } catch (IOException e) {
            System.err.println("Error when writing to player text file");
        }
    }
}