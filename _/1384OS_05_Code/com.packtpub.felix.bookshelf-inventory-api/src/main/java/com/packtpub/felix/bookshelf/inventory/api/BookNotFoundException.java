package com.packtpub.felix.bookshelf.inventory.api;

public class BookNotFoundException extends Exception
{

    private static final long serialVersionUID = -2507702292937029953L;

    public BookNotFoundException(String isbn) {
        super("Book not found: " + isbn);
    }
}
