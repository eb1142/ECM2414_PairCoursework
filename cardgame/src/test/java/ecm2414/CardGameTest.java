package ecm2414;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardGameTest {
    private int numPlayers;
    private ArrayList<Card> testPack;
    private ArrayList<Player> players;
    private ArrayList<Deck> decks;
    private CardGame.GameSetup gameSetup;


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
        gameSetup = new CardGame.GameSetup();
        //random num between 1 and 10
        Random rand = new Random();
        numPlayers = rand.nextInt(9) + 1;

        for (int i = 1; i <= 8*numPlayers; i++) {
            testPack.add(new Card(i));
        }
        
    }

    @Test
    public void testDistributeCards() {
        CardGame.distributeCards(numPlayers, testPack, players, decks);

        int counter = 1;
        for (Player player : players) {
            assertEquals(String.format("|%d|%d|%d|%d", counter, counter+numPlayers, counter+numPlayers*2, counter+numPlayers*3), player.handToString());
            counter++;
        }
        counter = 4*numPlayers + 1;
        for (Deck deck : decks) {
            assertEquals(String.format("|%d|%d|%d|%d", counter, counter+numPlayers, counter+numPlayers*2, counter+numPlayers*3), deck.cardsToString());
            counter++;
        }

    }

    @Test
    public void testRetrieveNumPlayers() {
        String input = String.format("%d\n", numPlayers);
        Scanner scanner = new Scanner(input);
        
        CardGame.retrieveNumPlayers(gameSetup, scanner);

        assertEquals(numPlayers, gameSetup.getNumPlayers());
    }

    @Test
    public void testRetrieveNumPlayersNegative() {
        String input = String.format("-1\n0\n-200\n%d\n", numPlayers);
        Scanner scanner = new Scanner(input);
        
        CardGame.retrieveNumPlayers(gameSetup, scanner);

        assertEquals(numPlayers, gameSetup.getNumPlayers());
    }

    @Test
    public void testRetrieveNumPlayersNoNum() {
        String input = String.format("ads\ndhytr6\ndhfg!3\n%d\n", numPlayers);
        Scanner scanner = new Scanner(input);
        
        CardGame.retrieveNumPlayers(gameSetup, scanner);

        assertEquals(numPlayers, gameSetup.getNumPlayers());
    }

    @Test
    public void testRetrieveNumPlayersNoInput() {
        String input = String.format("\n\n\n\n%d\n", numPlayers);
        Scanner scanner = new Scanner(input);
        
        CardGame.retrieveNumPlayers(gameSetup, scanner);

        assertEquals(numPlayers, gameSetup.getNumPlayers());
    }

    @Test
    public void testRetrievePackCards() {
        String input = "testing/test1.txt";
        Scanner scanner = new Scanner(input);

        testPack = new ArrayList<>();
        gameSetup.setNumPlayers(4);
        
        CardGame.retrievePackCards(gameSetup, scanner, testPack);
        String result = "";
        for (Card card : testPack) {
            result = result.concat("|" + Integer.toString(card.getValue()));
        }
        String expected = "";
        for (int i = 1; i <= 32; i++) {
            expected = expected.concat("|" + Integer.toString(i));
        }

        assertEquals(expected, result);
    }

    @Test
    public void testRetrievePackCardsNoFile() {
        String input = "testing/nofile.txt\ntesting/test1.txt";
        Scanner scanner = new Scanner(input);

        testPack = new ArrayList<>();
        gameSetup.setNumPlayers(4);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CardGame.retrievePackCards(gameSetup, scanner, testPack);

        System.setOut(originalOut);

        assertTrue(outContent.toString().contains("File not found in resources."));

        String result = "";
        for (Card card : testPack) {
            result = result.concat("|" + Integer.toString(card.getValue()));
        }
        String expected = "";
        for (int i = 1; i <= 32; i++) {
            expected = expected.concat("|" + Integer.toString(i));
        }

        assertEquals(expected, result);
    }
}
