package ecm2414;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CardTest {

    @Test
    public void testFaceValue () {
        Card card1 = new Card(4);
        assertEquals(4, card1.getValue());
        Card card2 = new Card(11);
        assertEquals(11, card2.getValue());
    }
}
