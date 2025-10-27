import org.junit.Test;
import org.junit.Assert;

public class DeckTest {
    @Test
    public void testDrawCard() {
        Deck deck = new Deck(1);
        Card cardToAdd = new Card(5);
        deck.addCard(cardToAdd);
        Assert.assertEquals(cardToAdd, deck.drawCard());
    }
}
