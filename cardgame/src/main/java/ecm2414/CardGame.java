package ecm2414;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

    //shared game state so player and threads can coordinate
    public static final AtomicBoolean gameOver = new AtomicBoolean(false);
    public static final AtomicInteger winnerID = new AtomicInteger(-1);

    public static void clearOldOutputs() {
        Path outputDir = Path.of("target", "output");

        if (Files.exists(outputDir)) {
            try (var paths = Files.list(outputDir)) {
                paths.forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        System.err.println("Could not delete file: " + path);
                    }
                });
            } catch (IOException e) {
                System.err.println("Error reading output directory: " + e.getMessage());
            }
        }
    }

    public static void distributeCards(int numPlayers, ArrayList<Card> packCards, ArrayList<Player> players, ArrayList<Deck> decks) {
        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player();
            players.add(player);
            Deck deck = new Deck();
            decks.add(deck);
            player.setDrawDeck(deck); //Makes sure each player draws from the correct deck
            if (players.size() > 1) {
                //Makes sure each player discards to the correct deck
                players.get(players.size()-2).setDiscardDeck(deck);
            }
        }
        players.get(players.size()-1).setDiscardDeck(decks.get(0)); //Makes the last player's deck wrap around

        for (int i = 0; i < 4 * numPlayers; i++) { //Deals 4 cards to each player in round robin way
            int playerIndex = i % numPlayers;
            players.get(playerIndex).addCard(packCards.get(i));
        }

        for (int i = 4 * numPlayers; i < packCards.size(); i++) { //Places the other cards into the decks round robin way
            int deckIndex = (i - 4 * numPlayers) % numPlayers;
            decks.get(deckIndex).addCard(packCards.get(i));
        }
    }

    public static void retrieveNumPlayers (GameSetup gameSetup, Scanner scanner) {
        //retrieve input for number of players
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
    }

    public static void retrievePackCards(GameSetup gameSetup, Scanner scanner, ArrayList<Card> packCards) {
            while (true) {
                packCards.clear();
                System.out.println("Please enter location of pack to load: ");
                String packLocation = scanner.nextLine().trim();

                try (InputStream in = CardGame.class.getResourceAsStream("/" + packLocation)) {
                //try (InputStream in = Files.newInputStream(Path.of(packLocation))) {
                    if (in == null) {
                        System.out.println("File not found in resources.");
                        continue;
                    }
                    
                    boolean cardCheck = true;
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (!line.isEmpty()) {
                                try {
                                    int faceValue = Integer.parseInt(line);
                                    packCards.add(new Card(faceValue));
                                } catch (NumberFormatException e) {
                                    System.out.println("This card is invalid: " + line);
                                    cardCheck = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!cardCheck)
                        continue;
                    if (packCards.size() != gameSetup.getNumPlayers() * 8) {
                        System.out.println("Incorrect number of cards in pack.");
                        continue;
                    }
                
                    gameSetup.setPackLocation(packLocation);
                    break;
                    
                } catch (IOException e) {
                    System.out.println("Error reading pack: " + e.getMessage());
                    //e.printStackTrace();
                }
            }
    }

    public static void signalGameOver(ArrayList<Deck> decks, int winner) {
        gameOver.set(true);
        winnerID.set(winner);
        for (Deck deck : decks) {
            synchronized (deck) {
                deck.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        GameSetup gameSetup = new GameSetup();
        ArrayList<Card> packCards = new ArrayList<>();
        clearOldOutputs();
        
        try (Scanner scanner = new Scanner(System.in)) {
            retrieveNumPlayers(gameSetup, scanner);
            retrievePackCards(gameSetup, scanner, packCards);
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return;
        }

        int numPlayers = gameSetup.getNumPlayers();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();

        System.out.println("Distributing cards...");
        distributeCards(numPlayers, packCards, players, decks);
        System.out.println("Cards distributed");
        
        gameOver.set(false);
        winnerID.set(-1);

        ArrayList<Thread> playerThreads = new ArrayList<>();

        for (Player player : players) {
            Thread playerThread = new Thread(() -> {
                while (!gameOver.get() && !Thread.currentThread().isInterrupted()) {
                    try {
                        if (player.checkWon()) {
                            if (winnerID.compareAndSet(-1, player.getID())) {
                                signalGameOver(decks, player.getID());
                                player.addToOutput(String.format("player %d wins", player.getID()));
                            }
                            break;
                        }
                        try {
                            player.turn();
                        } catch (Exception e) {
                            System.err.println("Error during player " + player.getID() + "'s turn: " + e.getMessage());
                        }
                    } catch (Exception ex) {
                        if (Thread.currentThread().isInterrupted()) {
                            break;
                        }
                    }
                }
            }, "thread-for-player-" + player.getID());
            playerThreads.add(playerThread);
            playerThread.start();
        }

        for (Thread thread : playerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (Deck deck : decks) {
            deck.writeFinalContents();
        }
    }
}
 