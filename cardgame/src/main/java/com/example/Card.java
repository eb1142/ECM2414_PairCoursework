package com.example;

public class Card {
    private int faceValue;

    public Card(int faceValue) {
        if (faceValue < 0) {
            throw new IllegalArgumentException("Face value must be non-negative.");
        }
        this.faceValue = faceValue;
    }

    public int getValue() {
        return faceValue;
    }
}