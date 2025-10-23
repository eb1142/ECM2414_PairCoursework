import java.util.Scanner;
import java.io.file;
import java.io.FileNotFoundException;

public class CardGame {
    public static class GameSetup { 
        private final int numPlayers;
        private final String packLocation;

        public GameSetup(int numPlayers, String packLocation) {
            this.numPlayers = numPlayers;
            this.packLocation = packLocation;
        }

        public int getNumPlayers() {
            return numPlayers;
        }

        public String getPackLocation() {
            return packLocation;
        }
    }

    public static GameSetup gameSetup = new GameSetup(); {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter number of players:");
            try {
                int numPlayers = scanner.next();
                if (numPlayers >= 1) {
                    gameSetup.setNumPlayers(numPlayers);
                    break;
                } else: {
                    System.out.println("Invalid number of players. Please enter a positive integer."); }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer."); }
        }

        while (true) {
            System.out.println("Please enter location of pack to load:");
            String packLocation = scanner.next();
            File pack = new File(packLocation);
            if (pack.exists() && pack.isFile()) {
                gameSetup.setPackLocation(packLocation);
                break;
            } else {
                System.out.println("File not found. Please enter a valid file path."); }
        }
    }
}
