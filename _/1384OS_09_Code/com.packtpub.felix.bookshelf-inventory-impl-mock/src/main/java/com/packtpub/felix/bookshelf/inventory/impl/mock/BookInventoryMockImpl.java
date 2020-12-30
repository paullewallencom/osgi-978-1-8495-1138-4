package com.packtpub.felix.bookshelf.inventory.impl.mock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookInventory;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.inventory.api.MutableBook;

/**
 * This is a quick-and-dirty implementation of the {@link BookInventory} interface. To be used as a
 * mock service while a database-based version is implemented
 */
public class BookInventoryMockImpl implements BookInventory
{

    /** The "default" group a book gets registered against when no group is set */
    public static final String DEFAULT_CATEGORY = "default";

    /** ISBN to Book */
    private Map<String, MutableBook> booksByISBN = new HashMap<String, MutableBook>();

    /** Category name to number of books having it */
    private Map<String, Integer> categories = new HashMap<String, Integer>();

    public MutableBook createBook(String isbn) {
        return new MutableBookImpl(isbn);
    }

    public Set<String> getCategories() {
        return this.categories.keySet();
    }

    /**
     * Store a book. This is either a create or an update of existing
     * 
     * @throws InvalidBookException
     *             if validation for an attribute has failed (e.g. mandatory attribute not set)
     */
    public String storeBook(MutableBook book) throws InvalidBookException {
        String isbn = book.getIsbn();
        if (isbn == null) {
            throw new InvalidBookException("ISBN is not set");
        }
        this.booksByISBN.put(isbn, book);
        String category = book.getCategory();
        if (category == null) {
            category = DEFAULT_CATEGORY;
        }
        if (this.categories.containsKey(category)) {
            int count = this.categories.get(category);
            this.categories.put(category, count + 1);
        }
        else {
            this.categories.put(category, 1);
        }
        return isbn;
    }

    /**
     * Removes a previously stored book based on its ISBN.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    public void removeBook(String isbn) throws BookNotFoundException {
        Book book = this.booksByISBN.remove(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        String category = book.getCategory();
        int count = this.categories.get(category);
        if (count == 1) {
            this.categories.remove(category);
        }
        else {
            this.categories.put(category, count - 1);
        }
    }

    /**
     * Load a previously stored book based on its ISBN, for read-only access.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    public Book loadBook(String isbn) throws BookNotFoundException {
        return loadBookForEdit(isbn);
    }

    /**
     * Load a previously stored book based on its ISBN, for read-write access.
     * 
     * @throws BookNotFoundException
     *             if not found.
     */
    public MutableBook loadBookForEdit(String isbn) throws BookNotFoundException {
        MutableBook book = this.booksByISBN.get(isbn);
        if (book == null) {
            throw new BookNotFoundException(isbn);
        }
        return book;
    }

    /**
     * Search for books that match the given criteria. See {@link SearchCriteria} for more details.
     */
    public Set<String> searchBooks(Map<SearchCriteria, String> criteria) {
        LinkedList<Book> books = new LinkedList<Book>();
        books.addAll(this.booksByISBN.values());
        // very inefficient search, but will do for this mock implementation
        // go through all books for each search criterion, and remove if it
        // doesnt match it
        for (Map.Entry<SearchCriteria, String> crit : criteria.entrySet()) {
            Iterator<Book> it = books.iterator();
            while (it.hasNext()) {
                Book book = it.next();
                switch (crit.getKey()) {
                    case AUTHOR_LIKE:
                        if (!checkStringMatch(book.getAuthor(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                    case ISBN_LIKE:
                        if (!checkStringMatch(book.getIsbn(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                    case CATEGORY_LIKE:
                        if (!checkStringMatch(book.getCategory(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                    case TITLE_LIKE:
                        if (!checkStringMatch(book.getTitle(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                    case RATING_GT:
                        if (!checkIntegerGreater(book.getRating(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                    case RATING_LT:
                        if (!checkIntegerSmaller(book.getRating(), crit.getValue())) {
                            it.remove();
                            continue;
                        }
                        break;
                }
            }
        }
        // copy isbns
        HashSet<String> isbns = new HashSet<String>();
        for (Book book : books) {
            isbns.add(book.getIsbn());
        }
        return isbns;
    }

    /**
     * Compare the integer attribute with that encoded as a string critVal.
     * 
     * @return true if <code>attr</code> is greater or equal to crit, or false otherwise.
     */
    private boolean checkIntegerGreater(int attr, String critVal) {
        int critValInt;
        try {
            critValInt = Integer.parseInt(critVal);
        }
        catch (NumberFormatException e) {
            return false;
        }
        if (attr >= critValInt) {
            return true;
        }
        return false;
    }

    /**
     * Compare the integer attribute with that encoded as a string critVal.
     * 
     * @return true if <code>attr</code> is less than or equal to crit, or false otherwise.
     */
    private boolean checkIntegerSmaller(int attr, String critVal) {
        int critValInt;
        try {
            critValInt = Integer.parseInt(critVal);
        }
        catch (NumberFormatException e) {
            return false;
        }
        if (attr <= critValInt) {
            return true;
        }
        return false;
    }

    /**
     * Compare the string attribute with criterion. If the criterion starts or ends with % then this
     * is considered a wild card ("123%" will match all values that start with "123")
     * 
     * @return true if there's a match, false otherwise.
     */
    private boolean checkStringMatch(String attr, String critVal) {
        if (attr == null) {
            return false;
        }
        attr = attr.toLowerCase();
        critVal = critVal.toLowerCase();

        boolean startsWith = critVal.startsWith("%");
        boolean endsWith = critVal.endsWith("%");

        if (startsWith && endsWith) {
            if (critVal.length() == 1) {
                return true;
            }
            else {
                return attr.contains(critVal.substring(1, critVal.length() - 1));
            }
        }
        else if (startsWith) {
            return attr.endsWith(critVal.substring(1));
        }
        else if (endsWith) {
            return attr.startsWith(critVal.substring(0, critVal.length() - 1));
        }
        else {
            return attr.equals(critVal);
        }
    }

}
