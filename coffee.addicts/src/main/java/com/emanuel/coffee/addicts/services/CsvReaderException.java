package com.emanuel.coffee.addicts.services;

public class CsvReaderException extends Exception{
    public CsvReaderException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
