package ecm2414;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class PlayerTest {
    private Player p1;
    private Player p2;
    private Deck deck;
    private ArrayList<Card> testCards;

    @BeforeEach
    public void setUp() {
        p1 = new Player();
        p2 = new Player();
        deck = new Deck();
        testCards = new ArrayList<>();
        for (int i = 1; i<= 5; i++) {
            testCards.add(new Card(i));
        }
    }

    @Test
    public void testPlayerIds() {
        assertEquals(Player.idCounter - 1, p1.getID());
        assertEquals(Player.idCounter, p2.getID());
    }
    @Test
    public void testAddAndRemoveCard() {
        p1.addCard(testCards.get(0));
        assertEquals("|1", p1.handToString());
        p1.removeCard(testCards.get(0));
        assertEquals("", p1.handToString());
    }

    @Test
    public void testDrawCard() {
        deck.addCard(testCards.get(0));
        deck.addCard(testCards.get(1));
        p1.setDrawDeck(deck);
        assertEquals("", p1.handToString());
        assertEquals("|1|2", deck.cardsToString());
        p1.drawCard();
        assertEquals("|1", p1.handToString());
        assertEquals("|2", deck.cardsToString());
    }

    @Test
    public void testDiscardCard() {
        deck.addCard(testCards.get(0));
        deck.addCard(testCards.get(1));
        p1.addCard(testCards.get(2));
        p1.setDiscardDeck(deck);
        Card discard = testCards.get(3);
        p1.addCard(discard);
        p1.discardCard(discard);
        assertEquals("|3", p1.handToString());
        assertEquals("|1|2|4", deck.cardsToString());
    }

    @Test
    public void testPickCardDuplicates() {
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+1));
        assertEquals(p1.getID()+1, p1.pickCard().getValue());
    }

    @Test
    public void testPickCardBeginning() {
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+3));
        assertEquals(p1.getID()+1, p1.pickCard().getValue());
    }

    @Test
    public void testPickCardEnd() {
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()+1));
        assertEquals(p1.getID()+1, p1.pickCard().getValue());
    }

    @Test
    public void testPickCardSame() {
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        assertEquals(1, p1.pickCard().getValue());
    }

    @Test
    public void testWinThreeSame() {
        p1.addCard(new Card(5));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        assertFalse(p1.checkWon());
    }

    @Test
    public void testWinNoneSame() {
        p1.addCard(new Card(5));
        p1.addCard(new Card(2));
        p1.addCard(new Card(1));
        p1.addCard(new Card(3));
        assertFalse(p1.checkWon());
    }

    @Test
    public void testWinAllSame() {
        p1.addCard(new Card(6));
        p1.addCard(new Card(6));
        p1.addCard(new Card(6));
        p1.addCard(new Card(6));
        assertTrue(p1.checkWon());
    }

    @Test
    public void testWinNotPlayer() {
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+1));
        assertTrue(p1.checkWon());
    }
}
