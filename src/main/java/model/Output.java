package model;

import java.util.List;

public class Output {
    private List<String> deck;
    private String message;
    private boolean gameFinished;

    public List<String> getDeck() {
        return deck;
    }

    public void setDeck(List<String> deck) {
        this.deck = deck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

}
