package com.packtpub.felix.bookshelf.service.impl;

import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.packtpub.felix.bookshelf.inventory.api.BookNotFoundException;
import com.packtpub.felix.bookshelf.inventory.api.InvalidBookException;
import com.packtpub.felix.bookshelf.service.api.BookAlreadyExistsException;
import com.packtpub.felix.bookshelf.service.api.BookshelfService;
import com.packtpub.felix.bookshelf.service.api.InvalidCredentialsException;

public class BookshelfServiceImplActivator implements BundleActivator
{
    ServiceRegistration reg = null;

    public void start(BundleContext context) throws Exception {
        this.reg = context.registerService(BookshelfService.class.getName(),
            new BookshelfServiceImpl(context), null);
        
        testService(context);
    }
    
    private void testService(BundleContext context) {
        // retrieve service
        ServiceReference ref = context.getServiceReference(BookshelfService.class.getName());
        if (ref==null) {
            throw new RuntimeException(
                "Service not registered: " + BookshelfService.class.getName());
        }
        BookshelfService service = (BookshelfService) context.getService(ref);
        // authenticate and get session
        String session;
        try {
            System.out.println("\nSigning in. . .");
            session = service.login("admin", "admin".toCharArray());
        }
        catch (InvalidCredentialsException e) {
            e.printStackTrace();
            return;
        }
        
        // add a few books
        try {
            System.out.println("\nAdding books. . .");
            service.addBook(session, "123-4567890100", "Book 1 Title", "John Doe", "Group 1", 0);
            service.addBook(session, "123-4567890101", "Book 2 Title", "Will Smith", "Group 1", 0);
            service.addBook(session, "123-4567890200", "Book 3 Title", "John Doe", "Group 2", 0);
            service.addBook(session, "123-4567890201", "Book 4 Title", "Jane Doe", "Group 2", 0);
        }
        catch (BookAlreadyExistsException e) {
            e.printStackTrace();
            return;
        }
        catch (InvalidBookException e) {
            e.printStackTrace();
            return;
        }
        // and test search
        String authorLike = "%Doe"; 
        System.out.println("Searching books with author: "+authorLike);
        Set<String> results = service.searchBooksByAuthor(session, authorLike);
        for (String isbn : results) {
            try {
                System.out.println(" - " + service.getBook(session, isbn));
            }
            catch (BookNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void stop(BundleContext context) throws Exception {
        if (this.reg!=null) {
            context.ungetService(reg.getReference());
        }
    }

}
