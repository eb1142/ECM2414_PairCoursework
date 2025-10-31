package ecm2414;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DeckTest {
    private Deck deck;
    private ArrayList<Card> testCards;

    @BeforeEach
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
        IndexOutOfBoundsException ex = assertThrows(IndexOutOfBoundsException.class, () -> deck.drawCard());
        assertEquals("Cannot draw from an empty deck", ex.getMessage());
        assertEquals("", deck.cardsToString());
    }
}
