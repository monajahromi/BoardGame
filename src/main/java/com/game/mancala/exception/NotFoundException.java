package com.game.mancala.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("requested resource not found");
    }
}
