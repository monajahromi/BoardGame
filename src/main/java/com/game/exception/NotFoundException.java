package com.game.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super("requested resource not found");
    }
}
