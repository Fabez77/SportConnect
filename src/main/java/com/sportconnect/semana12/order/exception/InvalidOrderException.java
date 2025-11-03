package com.sportconnect.semana12.order.exception;


import lombok.Getter;

@Getter
public class InvalidOrderException extends Exception {

    private final String message;

    public InvalidOrderException(String message) {
        super(message);
        this.message = message;
    }
}

