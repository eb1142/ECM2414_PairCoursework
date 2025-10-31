package ecm2414;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testDrawCard() throws Exception {
        deck.addCard(testCards.get(0));
        deck.addCard(testCards.get(1));
        p1.setDrawDeck(deck);
        assertEquals("", p1.handToString());
        assertEquals("|1|2", deck.cardsToString());
        
        Method drawCardMethod = Player.class.getDeclaredMethod("drawCard");
        drawCardMethod.setAccessible(true);

        drawCardMethod.invoke(p1);

        assertEquals("|1", p1.handToString());
        assertEquals("|2", deck.cardsToString());
    }

    @Test
    public void testDiscardCard() throws Exception {
        deck.addCard(testCards.get(0));
        deck.addCard(testCards.get(1));
        p1.addCard(testCards.get(2));
        p1.setDiscardDeck(deck);
        Card discard = testCards.get(3);
        p1.addCard(discard);
        
        Method discardCardMethod = Player.class.getDeclaredMethod("discardCard", Card.class);
        discardCardMethod.setAccessible(true);

        discardCardMethod.invoke(p1, discard);

        assertEquals("|3", p1.handToString());
        assertEquals("|1|2|4", deck.cardsToString());
    }

    /*
    @Test
    void testConcurrentDrawAndDiscard() throws InterruptedException {
        for (int i = 1; i <= 50; i++) deck.addCard(new Card(i));

        p1.setDrawDeck(deck);
        p2.setDiscardDeck(deck);

        Thread t1 = new Thread(() -> { for(int i=0;i<25;i++) p1.drawAndDiscard(); });
        Thread t2 = new Thread(() -> { for(int i=0;i<25;i++) p2.drawAndDiscard(); });

        t1.start(); t2.start();
        t1.join(); t2.join();

        assertEquals(0, deck.size());
    }
    */

    @Test
    public void testPickCardDuplicates() throws Exception {
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+1));

        Method pickCardMethod = Player.class.getDeclaredMethod("pickCard");
        pickCardMethod.setAccessible(true);

        Card picked = (Card) pickCardMethod.invoke(p1);
        assertNotNull(picked);
        assertNotEquals(p1.getID(), picked.getValue());
    }

    @Test
    public void testPickCardBeginning() throws Exception {
        p1.addCard(new Card(p1.getID()+1));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+2));
        p1.addCard(new Card(p1.getID()+3));

        Method pickCardMethod = Player.class.getDeclaredMethod("pickCard");
        pickCardMethod.setAccessible(true);

        Card picked = (Card) pickCardMethod.invoke(p1);
        assertNotNull(picked);
        assertNotEquals(p1.getID(), picked.getValue());
    }

    @Test
    public void testPickCardEnd() throws Exception{
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()));
        p1.addCard(new Card(p1.getID()+1));
        
        Method pickCardMethod = Player.class.getDeclaredMethod("pickCard");
        pickCardMethod.setAccessible(true);

        Card picked = (Card) pickCardMethod.invoke(p1);
        assertNotNull(picked);
        assertNotEquals(p1.getID(), picked.getValue());
    }

    @Test
    public void testPickCardSame() throws Exception {
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        p1.addCard(new Card(1));
        
        Method pickCardMethod = Player.class.getDeclaredMethod("pickCard");
        pickCardMethod.setAccessible(true);

        Card picked = (Card) pickCardMethod.invoke(p1);
        assertNotNull(picked);
        assertNotEquals(p1.getID(), picked.getValue());
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
