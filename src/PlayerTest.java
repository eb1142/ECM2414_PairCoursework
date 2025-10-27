
import org.junit.Test;
import org.junit.Assert;

public class PlayerTest {
    @Test
    public void testPlayerIds() {
        Player p1 = new Player();
        Player p2 = new Player();
        Assert.assertEquals(1, p1.getID());
        Assert.assertEquals(2, p2.getID());
    }
}
