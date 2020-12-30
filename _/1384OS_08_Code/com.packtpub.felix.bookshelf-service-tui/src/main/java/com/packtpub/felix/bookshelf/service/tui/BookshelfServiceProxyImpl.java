package com.packtpub.felix.bookshelf.service.tui;

import java.util.HashSet;
import java.util.Set;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServiceProxyImpl implements BookshelfServiceProxy
{
    private BundleContext context;

    public BookshelfServiceProxyImpl(BundleContext context) {
        this.context = context;
    }

    public String add(@Descriptor("username") String username,
                    @Descriptor("password") String password,
                    @Descriptor("ISBN") String isbn,
                    @Descriptor("Title") String title,
                    @Descriptor("Author") String author,
                    @Descriptor("Category") String category,
                    @Descriptor("Rating (0..10)") int rating)
        throws InvalidCredentialsException, BookAlreadyExistsException, InvalidBookException
    {
        BookshelfService service = lookupService(); 
        String sessionId = service.login(username, password.toCharArray());

        service.addBook(sessionId, isbn, title, author, category, rating);
        return isbn;
    }

    @Descriptor("Search books by author, title, or category")
    public Set<Book> search(
                    @Descriptor("username") String username,
                    @Descriptor("password") String password,
                    @Descriptor("search on attribute: author, title, or category") String attribute,
                    @Descriptor("match like (use % at the beginning or end of <like> for wild-card)") String filter)
        throws InvalidCredentialsException
    {
        BookshelfService service = lookupService();

        String sessionId = service.login(username, password.toCharArray());

        Set<String> results;
        
        if ("title".equals(attribute)) {
            results = service.searchBooksByTitle(sessionId, filter);
        }
        else if ("author".equals(attribute)) {
            results = service.searchBooksByAuthor(sessionId, filter);
        }
        else if ("category".equals(attribute)) {
            results = service.searchBooksByCategory(sessionId, filter);
        }
        else {
            throw new RuntimeException("Invalid attribute, expecting one of { 'title', 'author', 'category' } got '"+attribute+"'");
        }

        return getBooks(sessionId, service, results);
    }

    @Descriptor("Search books by rating")
    public Set<Book> search(@Descriptor("username") String username,
                    @Descriptor("password") String password,
                    @Descriptor("search on attribute: rating") String attribute,
                    @Descriptor("lower rating limit (inclusive)") int lower,
                    @Descriptor("upper rating limit (inclusive)") int upper)
        throws InvalidCredentialsException
    {
        if (!"rating".equals(attribute)) {
            throw new RuntimeException("Invalid attribute, expecting 'rating' got '"+attribute+"'");
        }
        BookshelfService service = lookupService();

        String sessionId = service.login(username, password.toCharArray());

        Set<String> results = service.searchBooksByRating(sessionId, lower, upper);
        
        return getBooks(sessionId, service, results);
    }

    protected BookshelfService lookupService() {
        ServiceReference reference = context.getServiceReference(BookshelfService.class.getName());
        if (reference == null) {
            throw new RuntimeException("BookshelfService not registered, cannot invoke operation");
        }

        BookshelfService service = (BookshelfService) this.context.getService(reference);
        if (service == null) {
            // could have been unregistered since above lookup
            throw new RuntimeException("BookshelfService not registered, cannot invoke operation");
        }
        return service;
    }

    private Set<Book> getBooks(String sessionId, BookshelfService service, Set<String> results) {
        Set<Book> books = new HashSet<Book>();
        for (String isbn : results) {
            Book book;
            try {
                book = service.getBook(sessionId, isbn);
                books.add(book);
            }
            catch (BookNotFoundException e) {
                System.err.println("ISBN " + isbn + " referenced but not found");
            }
        }
        return books;
    }

}
