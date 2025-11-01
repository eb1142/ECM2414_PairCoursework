package ecm2414;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardGameTest {
    private int numPlayers;
    private ArrayList<Card> testPack;
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;


    public static void clearOldOutputs() {
        Path outputDir = Path.of("target", "output");

        if (Files.exists(outputDir)) {
            try (var paths = Files.list(outputDir)) {
                for (Path path : paths.toList()) {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("Could not delete file: " + path);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading output directory: " + e.getMessage());
            }
        }
    }

    @BeforeEach
    public void setUp() {
        testPack = new ArrayList<>();
        players = new ArrayList<>();
        decks = new ArrayList<>();
    }

    @Test
    public void testDistributeCards() {
        numPlayers = 4;
        for (int i = 1; i <= 32; i++) {
            testPack.add(new Card(i));
        }
        CardGame.distributeCards(numPlayers, testPack, players, decks);

        int counter = 1;
        for (Player player : players) {
            assertEquals(String.format("|%d|%d|%d|%d", counter, counter+numPlayers, counter+numPlayers*2, counter+numPlayers*3), player.handToString());
            counter++;
        }
        counter = 17;
        for (Deck deck : decks) {
            assertEquals(String.format("|%d|%d|%d|%d", counter, counter+numPlayers, counter+numPlayers*2, counter+numPlayers*3), deck.cardsToString());
            counter++;
        }

    }
}
