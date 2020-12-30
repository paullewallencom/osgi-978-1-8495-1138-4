package com.packtpub.felix.bookshelf.service.api;

import java.util.Set;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;

public interface BookshelfService extends Authentication
{
    Set<String> getGroups(String session);

    void addBook(String session, String isbn, String title, String author, String group, int grade)
                    throws BookAlreadyExistsException, InvalidBookException;

    void modifyBookGroup(String session, String isbn, String group) throws BookNotFoundException,
                    InvalidBookException;

    void modifyBookGrade(String session, String isbn, int grade) throws BookNotFoundException,
                    InvalidBookException;

    void removeBook(String session, String isbn) throws BookNotFoundException;

    Book getBook(String session, String isbn) throws BookNotFoundException;

    Set<String> searchBooksByGroup(String session, String groupLike);

    Set<String> searchBooksByAuthor(String session, String authorLike);

    Set<String> searchBooksByTitle(String session, String titleLike);

    Set<String> searchBooksByGrade(String session, int gradeLower, int gradeUpper);
}
