import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

import java.util.ArrayList;

public class DeckTest {
    private Deck deck;
    private ArrayList<Card> testCards;

    @Before
    public void setUp() {
        deck = new Deck();
        testCards = new ArrayList<>();
        for (int i = 1; i<= 5; i++) {
            testCards.add(new Card(i));
        }
    }

    @Test
    public void testDeckIds() {
        assertEquals(Deck.idCounter, deck.getID());
    }

    @Test
    public void testAddAndDrawCard() {
        deck.addCard(testCards.get(0));
        deck.addCard(testCards.get(2));
        deck.addCard(testCards.get(1));
        assertEquals("|1|3|2", deck.cardsToString());
        deck.drawCard();
        assertEquals("|3|2", deck.cardsToString());
    }

    @Test
    public void testDrawEmpty() {
        assertThrows(IndexOutOfBoundsException.class, () -> deck.drawCard());
        assertEquals("", deck.cardsToString());
    }
}
