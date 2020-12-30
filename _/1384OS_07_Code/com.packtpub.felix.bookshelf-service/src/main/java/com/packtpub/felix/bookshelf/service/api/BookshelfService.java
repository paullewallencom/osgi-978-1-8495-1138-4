package com.packtpub.felix.bookshelf.service.api;

import java.util.Set;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;

public interface BookshelfService extends Authentication
{
    Set<String> getCategories(String sessionId);

    void addBook(String session, String isbn, String title, String author, String category,
                    int rating) throws BookAlreadyExistsException, InvalidBookException;

    void modifyBookCategory(String session, String isbn, String category)
                    throws BookNotFoundException, InvalidBookException;

    void modifyBookRating(String session, String isbn, int rating) throws BookNotFoundException,
                    InvalidBookException;

    void removeBook(String session, String isbn) throws BookNotFoundException;

    Book getBook(String session, String isbn) throws BookNotFoundException;

    Set<String> searchBooksByCategory(String session, String categoryLike);

    Set<String> searchBooksByAuthor(String session, String authorLike);

    Set<String> searchBooksByTitle(String session, String titleLike);

    Set<String> searchBooksByRating(String session, int ratingLower, int ratingUpper);

}
