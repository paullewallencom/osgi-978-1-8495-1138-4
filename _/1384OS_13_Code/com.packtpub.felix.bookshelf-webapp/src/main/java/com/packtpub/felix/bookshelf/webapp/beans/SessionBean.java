package com.packtpub.felix.bookshelf.webapp.beans;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.packtpub.felix.bookshelf.service.api.BookshelfService;

public class SessionBean
{
    static final String OSGI_BUNDLECONTEXT = "osgi-bundlecontext"; 
    
    private BundleContext ctx;
    
    private String sessionId;

    public void initialize(ServletContext context) {
        this.ctx = (BundleContext) context.getAttribute(OSGI_BUNDLECONTEXT);
    }
    
    public BookshelfService getBookshelf() {
        ServiceReference ref = ctx.getServiceReference(BookshelfService.class.getName());
        BookshelfService bookshelf = (BookshelfService) ctx.getService(ref);
        return bookshelf;
    }

    public String getSessionId() {
        return sessionId;
    }
    
    public boolean sessionIsValid() {
        return getBookshelf().sessionIsValid(getSessionId());
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    
}
