package ecm2414;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {
    public static class GameSetup { 
        private int numPlayers;
        private String packLocation;

        public GameSetup() {}

        public GameSetup(int numPlayers, String packLocation) {
            this.numPlayers = numPlayers;
            this.packLocation = packLocation;
        }

        public int getNumPlayers() {
            return numPlayers;
        }

        public void setNumPlayers(int numPlayers) {
            this.numPlayers = numPlayers;
        }

        public String getPackLocation() {
            return packLocation;
        }

        public void setPackLocation(String packLocation) {
            this.packLocation = packLocation;
        }
    }

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
            System.out.println("Old output files cleared.");
        } catch (IOException e) {
            System.err.println("Error reading output directory: " + e.getMessage());
        }
    }
}

    public static void main(String[] args) {
        GameSetup gameSetup = new GameSetup();
        ArrayList<Card> packCards = new ArrayList<>();
        clearOldOutputs();
        
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Please enter number of players: ");
                try {
                    int numPlayers = Integer.parseInt(scanner.nextLine());
                    if (numPlayers >= 1) {
                        gameSetup.setNumPlayers(numPlayers);
                        break;
                    } else {
                        System.out.println("Invalid number of players. Please enter a positive integer."); }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a positive integer."); }
            }

            while (true) {
                System.out.println("Please enter location of pack to load: ");
                String packLocation = scanner.nextLine().trim();
                try (InputStream in = CardGame.class.getResourceAsStream("/" + packLocation)) {
                    if (in == null) {
                        System.out.println("File not found in resources.");
                        continue;
                    }

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.isEmpty()) {
                                try {
                                    int faceValue = Integer.parseInt(line);
                                    packCards.add(new Card(faceValue));
                                } catch (NumberFormatException e) {
                                    System.out.println("This card is invalid: " + line);
                                    return;
                                }
                            }
                        }
                    }
                
                    gameSetup.setPackLocation(packLocation);
                    break;
                    
                } catch (IOException e) {
                    System.out.println("Error reading pack: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }

        int numPlayers = gameSetup.getNumPlayers();
        if (packCards.size() != numPlayers * 8) {
            System.out.println("Incorrect number of cards in pack.");
            return;
        }

        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();

        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player();
            players.add(player);
            Deck deck = new Deck();
            decks.add(deck);
            player.setDrawDeck(deck);
            if (players.size() > 1) {
                players.get(players.size()-2).setDiscardDeck(deck);
            }
        }
        players.get(players.size()-1).setDiscardDeck(decks.get(0));

        for (int i = 0; i < 4 * numPlayers; i++) {
            int playerIndex = i % numPlayers;
            players.get(playerIndex).addCard(packCards.get(i));
        }

        for (int i = 4 * numPlayers; i < packCards.size(); i++) {
            int deckIndex = (i - 4 * numPlayers) % numPlayers;
            decks.get(deckIndex).addCard(packCards.get(i));
        }

    }
}
 