package com.packtpub.felix.bookshelf.service.tui;

import java.io.PrintStream;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.osgi.framework.BundleContext;

import com.packtpub.felix.bookshelf.inventory.api.Book;
import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class SearchBooksCommand extends AbstractBookshelfCommand
{
    public SearchBooksCommand(BundleContext context) {
        super(context);
    }

    public String getName() {
        return "book-search";
    }

    public String getShortDescription() {
        return "Search books by author, title, category or rating";
    }

    public String getUsage() {
        return "book-search username password author <like>\n"
                        + "book-search username password title <like>\n"
                        + "book-search username password category <like>\n"
                        + "book-search username password rating <gt> <lt>\n"
                        + "(use % at the beginning or end of <like> for wild-card)";
    }

    public void execute(String line, PrintStream out, PrintStream err) {
        CommandLine command = CommandLine.parse(line);
        int len = command.getArguments().length; 
        if (len < 4 || len > 5) {
            throw new InvalidCommandAttributesRuntimeException(
                "Got " + len + " arguments, expecting 4 or 5");
        }

        String username = command.getArguments()[0];
        String password = command.getArguments()[1];
        String attr = command.getArguments()[2];
        String val1 = trimQuotes(command.getArguments()[3]);
        String val2 = null;
        if ("rating".equals(attr)) {
            if (len==5) {
                val2 = trimQuotes(command.getArguments()[4]);
            }
            else {
                throw new InvalidCommandAttributesRuntimeException(
                    "Search on " + attr + " needs 5 arguments");
            }
        }

        BookshelfService service = lookupService();

        String session;
        try {
            session = service.login(username, password.toCharArray());
        }
        catch (InvalidCredentialsException e) {
            err.println("Invalid credentials");
            return;
        }
        Set<String> results;
        
        if ("title".equals(attr)) {
            results = service.searchBooksByTitle(session, val1);
        }
        else if ("author".equals(attr)) {
            results = service.searchBooksByAuthor(session, val1);
        }
        else if ("category".equals(attr)) {
            results = service.searchBooksByCategory(session, val1);
        }
        else if ("rating".equals(attr)) {
            int lower = Integer.parseInt(val1);
            int upper = Integer.parseInt(val2);

            results = service.searchBooksByRating(session, lower, upper);
        }
        else {
            err.println("Invalid criterion: " + attr);
            return;
        }

        for (String isbn : results) {
            Book book;
            try {
                book = service.getBook(session, isbn);
                out.println(book.toString());
            }
            catch (BookNotFoundException e) {
                // should not happen!
                err.println("ISBN " + isbn + " referenced but not found");
            }
        }
    }
}
