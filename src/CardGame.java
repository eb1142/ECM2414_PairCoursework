import java.util.*;
import java.io.*;

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

        public int setNumPlayers(int numPlayers) {
            return this.numPlayers = numPlayers;
        }

        public String getPackLocation() {
            return packLocation;
        }

        public void setPackLocation(String packLocation) {
            this.packLocation = packLocation;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameSetup gameSetup = new GameSetup();

        while (true) {
            System.out.println("Please enter number of players:");
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
            System.out.println("Please enter location of pack to load:");
            String packLocation = scanner.nextLine();
            File pack = new File(packLocation);
            if (pack.exists() && pack.isFile()) {
                gameSetup.setPackLocation(packLocation);
                break;
            } else {
                System.out.println("File not found. Please enter a valid file path."); }
        } scanner.close();

        List<Card> packCards = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(gameSetup.getPackLocation()))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
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
        } catch (FileNotFoundException e) {
            System.out.println("Pack location saved incorrectly.");
            return;
        }

        int numPlayers = gameSetup.getNumPlayers();
        if (packCards.size() != numPlayers * 8) {
            System.out.println("Incorrect number of cards in pack.");
            return;
        }

        List<Player> players = new ArrayList<>();
        List<Deck> decks = new ArrayList<>();

        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player();
            players.add(player);
            Deck deck = new Deck(player.getID());
            decks.add(deck);
        }

        for (int i = 0; i < 4 * numPlayers; i++) {
            int playerIndex = i % numPlayers;
            players.get(playerIndex).addCard(packCards.get(i));
        }

        for (int i = 4 * numPlayers; i < packCards.size(); i++) {
            int deckIndex = ((i - 4) % numPlayers) % numPlayers;
            decks.get(deckIndex).addCard(packCards.get(i));
        }

    }
}
 