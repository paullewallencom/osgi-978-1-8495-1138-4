package com.packtpub.felix.bookshelf.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.MutableBook;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory.SearchCriteria;
import com.packtpub.felix.bookshelf.log.api.BookshelfLogHelper;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServiceImpl implements BookshelfService
{
    private String sessionId;

    BookInventory inventory;
    BookshelfLogHelper logger;
    
    private BookshelfLogHelper getLogger()
    {
        return this.logger;
    }
    
    private BookInventory lookupBookInventory() {
        return this.inventory;
    }

    public String login(String username, char[] password) throws InvalidCredentialsException {
        getLogger().debug(LoggerConstants.LOG_LOGIN_ATTEMPT, username);
        if ("admin".equals(username) && Arrays.equals(password, "admin".toCharArray())) {
            this.sessionId = Long.toString(System.currentTimeMillis());
            getLogger().debug(LoggerConstants.LOG_LOGIN_SUCCESS, username, sessionId);
            return this.sessionId;
        }
        throw new InvalidCredentialsException(username);
    }

    public void logout(String sessionId) {
        getLogger().debug(LoggerConstants.LOG_LOGOUT_SESSION, sessionId);
        checkSession(sessionId);
        this.sessionId = null;
    }
    
    public boolean sessionIsValid(String sessionId) {
        return this.sessionId!=null && this.sessionId.equals(sessionId);
    }

    protected void checkSession(String sessionId) {
        if (!sessionIsValid(sessionId)) {
            throw new SessionNotValidRuntimeException(sessionId);
        }
    }

    public Book getBook(String sessionId, String isbn) throws BookNotFoundException {
        getLogger().debug(LoggerConstants.LOG_GET_BY_ISBN, isbn);
        checkSession(sessionId);
        BookInventory inventory = lookupBookInventory();
        return inventory.loadBook(isbn);
    }

    public MutableBook getBookForEdit(String sessionId, String isbn) throws BookNotFoundException {
        getLogger().debug(LoggerConstants.LOG_EDIT_BY_ISBN, isbn);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        return inv.loadBookForEdit(isbn);
    }

    public void addBook(String sessionId, String isbn, String title, String author, String category,
                    int rating) throws BookAlreadyExistsException, InvalidBookException {
        getLogger().debug(LoggerConstants.LOG_ADD_BOOK,
            isbn, title, author, category, rating);
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        getLogger().debug(LoggerConstants.LOG_CREATE_BOOK, isbn);
        MutableBook book = inv.createBook(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setRating(rating);

        getLogger().debug(LoggerConstants.LOG_STORE_BOOK, isbn);
        inv.storeBook(book);
    }

    public void modifyBookCategory(String sessionId, String isbn, String category)
                    throws BookNotFoundException, InvalidBookException {
        getLogger().debug(LoggerConstants.LOG_MODIFY_CATEGORY, isbn, category);
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setCategory(category);

        inv.storeBook(book);
    }

    public void modifyBookRating(String sessionId, String isbn, int rating)
                    throws BookNotFoundException, InvalidBookException {
        getLogger().debug(LoggerConstants.LOG_MODIFY_RATING, isbn, rating);
        checkSession(sessionId);

        BookInventory inv = lookupBookInventory();

        MutableBook book = inv.loadBookForEdit(isbn);
        book.setRating(rating);

        inv.storeBook(book);
    }

    public Set<String> getCategories(String sessionId) {
        getLogger().debug(LoggerConstants.LOG_GET_CATEGORIES);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        return inv.getCategories();
    }

    public void removeBook(String sessionId, String isbn) throws BookNotFoundException {
        getLogger().debug(LoggerConstants.LOG_REMOVE_BOOK, isbn);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        inv.removeBook(isbn);
    }

    public Set<String> searchBooksByAuthor(String sessionId, String authorLike) {
        getLogger().debug(LoggerConstants.LOG_SEARCH_BY_AUTHOR, authorLike);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.AUTHOR_LIKE, authorLike);
        return inv.searchBooks(criteria);
    }

    public Set<String> searchBooksByCategory(String sessionId, String categoryLike) {
        getLogger().debug(LoggerConstants.LOG_SEARCH_BY_CATEGORY, categoryLike);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.CATEGORY_LIKE, categoryLike);
        return inv.searchBooks(criteria);
    }

    public Set<String> searchBooksByTitle(String sessionId, String titleLike) {
        getLogger().debug(LoggerConstants.LOG_SEARCH_BY_TITLE, titleLike);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.TITLE_LIKE, titleLike);
        return inv.searchBooks(criteria);
    }

    public Set<String> searchBooksByRating(String sessionId, int gradeLower, int gradeUpper) {
        getLogger().debug(LoggerConstants.LOG_SEARCH_BY_RATING, gradeLower, gradeUpper);
        checkSession(sessionId);
        BookInventory inv = lookupBookInventory();
        Map<SearchCriteria, String> criteria = new HashMap<SearchCriteria, String>();
        criteria.put(SearchCriteria.RATING_GT, Integer.toString(gradeLower));
        criteria.put(SearchCriteria.RATING_LT, Integer.toString(gradeUpper));
        return inv.searchBooks(criteria);
    }

}
