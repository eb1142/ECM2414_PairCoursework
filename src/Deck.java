import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int deckNum;

    public Deck(int deckNum) {
        if (deckNum < 0) {
            throw new IllegalArgumentException("Deck number must be non-negative.");
        }
        this.deckNum = deckNum;
        this.cards = new ArrayList<>();
    }

    public int getDeckID () {
        return deckNum;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public synchronized Card drawCard() {
        return cards.remove(0);
    }
    public void addDiscardedCard (Card card) {
        cards.add(card);
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

