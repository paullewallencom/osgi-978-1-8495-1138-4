package com.packtpub.felix.bookshelf.inventory.api;

/** Extends the {@link Book} interface making it mutable. */
public interface MutableBook extends Book
{

    /** Set the book reference number (ISBN) */
    void setIsbn(String isbn);

    /** Set the book title */
    void setTitle(String title);

    /** Set the book author */
    void setAuthor(String author);

    /** Set the book category */
    void setCategory(String category);

    /** Set the awarded rating (0 to 10) */
    void setRating(int rating);
}
