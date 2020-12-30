package com.packtpub.felix.bookshelf.service.tui;

import org.apache.felix.shell.Command;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.packtpub.felix.bookshelf.service.api.BookshelfService;

public abstract class AbstractBookshelfCommand implements Command
{
    private BundleContext context;

    public AbstractBookshelfCommand(BundleContext context) {
        this.context = context;
    }

    protected BookshelfService lookupService() {
        ServiceReference reference = context.getServiceReference(BookshelfService.class.getName());
        if (reference == null) {
            throw new RuntimeException("BookshelfService not registered, cannot invoke operation");
        }

        BookshelfService service = (BookshelfService) this.context.getService(reference);
        if (service == null) {
            throw new RuntimeException("BookshelfService not registered, cannot invoke operation");
        }
        return service;
    }

    protected String trimQuotes(String txt) {
        if (txt.startsWith("\"") && txt.endsWith("\"")) {
            txt = txt.substring(1, txt.length() - 1);
        }
        return txt;
    }
}
