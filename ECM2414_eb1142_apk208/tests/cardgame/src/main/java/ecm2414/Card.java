package ecm2414;

public class Card {
    private int faceValue;

    public Card(int faceValue) {
        if (faceValue < 1) {
            throw new IllegalArgumentException("Face value must be non-negative.");
        }
        this.faceValue = faceValue;
    }

    public int getValue() {
        return faceValue;
    }

    @Override
    public String toString() {
        return Integer.toString(faceValue);
    }
}