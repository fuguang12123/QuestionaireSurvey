package com.example.survey.exception;

public class InvalidSubmissionDataException extends RuntimeException {
    public InvalidSubmissionDataException(String message) {
        super(message);
    }
}