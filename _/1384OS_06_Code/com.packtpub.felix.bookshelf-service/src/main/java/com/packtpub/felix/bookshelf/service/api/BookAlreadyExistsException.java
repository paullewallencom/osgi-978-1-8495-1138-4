package com.packtpub.felix.bookshelf.service.api;

public class BookAlreadyExistsException extends Exception
{

    private static final long serialVersionUID = -6820459281088561968L;

    public BookAlreadyExistsException(String isbn) {
        super("Book with isbn '"+isbn+"' already exists");
    }
}
