package ecm2414;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    static int idCounter = 0;
    private final int playerNum;
    private final ArrayList<Card> hand;
    private Deck discardDeck;
    private Deck drawDeck;

    public Player() {
        playerNum = ++idCounter;
        hand = new ArrayList<>();
    }
    public void setDrawDeck(Deck deck) {
        drawDeck = deck;
    }
    public void setDiscardDeck(Deck deck) {
        discardDeck = deck;
    }

    public int getID() {
        return playerNum;
    }

    // for testing
    public String handToString() {
        String result = "";
        for (Card card : hand) {
            result = result.concat("|" + Integer.toString(card.getValue()));
        }
        return result;
    }
    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }
    //adds card to hand from head of deck
    private void drawCard() {
        Card card = drawDeck.drawCard();
        addCard(card);
        addToOutput(String.format("player %d draws a %d from deck %d", playerNum, card.getValue(), drawDeck.getID()));
    }

    //adds a card to bottom of deck and removes from hand
    private void discardCard (Card card) {
        removeCard(card);
        synchronized (discardDeck) {
            discardDeck.addCard(card);
            discardDeck.notifyAll(); 
        }
        addToOutput(String.format("player %d discards a %d to deck %d", playerNum, card.getValue(), discardDeck.getID()));
    }

    //makes drawCard and discardCard one atomic action
    //a turn of a play for a player
    public void turn() {
        // If player already won, exit
        if (checkWon() || CardGame.gameOver.get()) {
            return;
        }

        // Determine lock order to prevent deadlock
        Object firstLock = drawDeck;
        Object secondLock = discardDeck;
        if (System.identityHashCode(firstLock) > System.identityHashCode(secondLock)) {
            Object temp = firstLock;
            firstLock = secondLock;
            secondLock = temp;
        }

        // Wait until draw deck has a card
        Card drawnCard = null;
        synchronized (drawDeck) {
            while (drawDeck.isEmpty() && !CardGame.gameOver.get()) {
                try {
                    drawDeck.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            // If game ended while waiting, exit
            if (CardGame.gameOver.get()) {
                return;
            }

            // Draw card
            drawnCard = drawDeck.drawCard();
        }

        // Add drawn card to hand and notify discard deck inside locks
        synchronized (firstLock) {
            synchronized (secondLock) {
                addCard(drawnCard);
                addToOutput(String.format("player %d draws a %d from deck %d",
                        playerNum, drawnCard.getValue(), drawDeck.getID()));

                // Pick a card to discard
                Card toDiscard = pickCard();
                removeCard(toDiscard);

                // Add to discard deck and notify waiting players
                synchronized (discardDeck) {
                    discardDeck.addCard(toDiscard);
                    discardDeck.notifyAll();
                }

                addToOutput(String.format("player %d discards a %d to deck %d",
                        playerNum, toDiscard.getValue(), discardDeck.getID()));

                // Log current hand
                addToOutput(String.format("player %d current hand is %d %d %d %d",
                        playerNum,
                        hand.get(0).getValue(),
                        hand.get(1).getValue(),
                        hand.get(2).getValue(),
                        hand.get(3).getValue()));
            }
        }
    }


    //picks card that doesn't equal the player number
    private Card pickCard() {
        ArrayList<Card> temp = new ArrayList<>();
        for (Card card : hand) {
            if (card.getValue() != playerNum) {
                temp.add(card);
            }
        }
        Random rand = new Random();

        if (!temp.isEmpty()) {
            return temp.get(rand.nextInt(temp.size()));
        } else {
            /*this is where cards are all right
            , doesn't matter which is picked*/
            return hand.get(0);
        }
    }

    public boolean checkWon() {
        //checks if every card is the same value in hand
        int current = hand.get(0).getValue();
        for (Card card : hand) {
            if (card.getValue() != current)
                return false;
        }
        return true;
    }

    public synchronized void addToOutput(String text) {
        String filename = String.format("player%d_output.txt", playerNum);
        try {
            Path outputDir = Path.of("target", "output");
            Files.createDirectories(outputDir);

            Path outputFile = outputDir.resolve(filename);

            try (FileWriter writer = new FileWriter(outputFile.toFile(), true)) {
                writer.write(text + "\n");
            }

        } catch (IOException e) {
            System.err.println("Error when writing to player text file");
        }
    }
}