package com.julian.rectangles.application;

public class InvalidRectangleException extends RuntimeException {

    public InvalidRectangleException(String errorMessage) {
        super(errorMessage);
    }

}
