package com.sportconnect.semana12.order.exception;


import lombok.Getter;

@Getter
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}


