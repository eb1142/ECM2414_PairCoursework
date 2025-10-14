import java.util.ArrayList;

public class Player {
    private int playerNum;
    private ArrayList<Card> hand;

    public Player(int playerNum) {
        if (playerNum < 0) {
            throw new IllegalArgumentException("Player number must be non-negative.");
        }
        this.playerNum = playerNum;
        this.hand = new ArrayList<>()
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        return hand.remove(card);
    }
}