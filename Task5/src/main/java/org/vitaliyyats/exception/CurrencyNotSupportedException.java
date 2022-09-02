package org.vitaliyyats.exception;

// is it correct way to create custom exception?
public class CurrencyNotSupportedException extends Exception {
    public CurrencyNotSupportedException(String s) {
        super(s);
    }
}
