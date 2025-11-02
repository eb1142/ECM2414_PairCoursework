package ecm2414;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cards;
    static int idCounter = 0;
    private final int deckNum;

    public Deck() {
        this.deckNum = ++idCounter;
        this.cards = new ArrayList<>();
    }

    public int getID () {
        return deckNum;
    }

    public synchronized void addCard(Card card) {
        if (card == null) return;
        cards.add(card);
    }

    public synchronized Boolean isEmpty() {
        return cards.isEmpty();
    }

    public synchronized int size() {
        return cards.size();
    }

    public synchronized Card drawCard() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Cannot draw from an empty deck");
        }
            return cards.remove(0);
    }

    // for testing
    public String cardsToString() {
        String result = "";
        for (Card card : cards) {
            result = result.concat("|" + Integer.toString(card.getValue()));
        }
        return result;
    }

    public synchronized void writeFinalContents() {
        String filename = "deck" + deckNum + "_output.txt";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card : cards) {
                writer.write(card.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing deck: " + e.getMessage());
        }
    }
}

