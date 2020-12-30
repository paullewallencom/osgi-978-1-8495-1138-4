package com.packtpub.felix.bookshelf.inventory.api;

public class InvalidBookException extends Exception
{

    private static final long serialVersionUID = -2507702292937029953L;

    public InvalidBookException(String message) {
        super("Book invalid: " + message);
    }
}
