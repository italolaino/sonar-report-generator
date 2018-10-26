package com.srg.model;

public class AnalyserException extends Exception {

    public AnalyserException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalyserException(Throwable cause) {
        super(cause);
    }
}
