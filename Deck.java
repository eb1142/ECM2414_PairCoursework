import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

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

    public synchronized void drawCard(Player player) {
        Card drawnCard = cards.remove(0);
        player.addCardToHand(drawnCard);
        Card discardedCard = cards.remove(0);
        cards.add(discardedCard);
    }

    public synchronized void writeFinalContents() {
        String filename = "deck" + deckNum + "_output.txt";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Card card : cards) {
                writer.write(card.toString() + "\n");
            }
        } catch (IOException e) {
            system.err.println("Error writing deck: " + e.getMessage());)
        }
    }
}

