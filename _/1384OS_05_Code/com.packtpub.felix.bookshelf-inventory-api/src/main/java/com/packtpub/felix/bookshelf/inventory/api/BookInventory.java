package com.packtpub.felix.bookshelf.inventory.api;

import java.util.Map;
import java.util.Set;

public interface BookInventory
{

    /**
     * Search criteria enum for the {@link BookInventory#searchBooks(Map)} method
     */
    enum SearchCriteria {
        /** ISBN compare, use % at beginning or end of a criterion for wildcard. */
        ISBN_LIKE,
        /** Title compare, use % at beginning or end of a criterion for wildcard. */
        TITLE_LIKE,
        /** Author compare, use % at beginning or end of a criterion for wildcard. */
        AUTHOR_LIKE,
        /** CATEGORY compare, use % at beginning or end of a criterion for wildcard. */
        CATEGORY_LIKE,
        /** Rating compare, greater or equal to criterion */
        RATING_GT,
        /** RATING compare, less than or equal to criterion */
        RATING_LT
    }

    /**
     * Create an editable book bean. This does not store the book to the repository
     */
    MutableBook createBook(String isbn) throws BookAlreadyExistsException;

    /**
     * Store a book. This is either a create or an update of existing
     * 
     * @throws InvalidBookException
     *             if validation for an attribute has failed (e.g. mandatory attribute not set)
     */
    String storeBook(MutableBook book) throws InvalidBookException;

    /**
     * Load a previously stored book based on its ISBN, for read-only access.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    Book loadBook(String isbn) throws BookNotFoundException;

    /**
     * Load a previously stored book based on its ISBN, for read-write access.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    MutableBook loadBookForEdit(String isbn) throws BookNotFoundException;

    /**
     * Removes a previously stored book based on its ISBN.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    void removeBook(String isbn) throws BookNotFoundException;

    /**
     * Search for books that match the given criteria. See {@link SearchCriteria} for more details.
     */
    Set<String> searchBooks(Map<SearchCriteria, String> criteria);

    /** Get the list the book categories. */
    Set<String> getCategories();
}
