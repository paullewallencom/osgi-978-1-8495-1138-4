package com.packtpub.felix.bookshelf.inventory.api;

public class BookAlreadyExistsException extends Exception
{

    private static final long serialVersionUID = 4624205886447737814L;

    public BookAlreadyExistsException(String isbn) {
        super("Book already exists: " + isbn);
    }
}
