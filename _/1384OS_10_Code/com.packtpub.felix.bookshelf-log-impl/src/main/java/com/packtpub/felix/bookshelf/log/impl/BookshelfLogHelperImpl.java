package com.packtpub.felix.bookshelf.log.impl;

import java.text.MessageFormat;

import org.osgi.service.log.LogService;

import com.packtpub.felix.bookshelf.log.api.BookshelfLogHelper;

public class BookshelfLogHelperImpl implements BookshelfLogHelper
{
    LogService log;

    public void debug(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_DEBUG, message);
    }

    public void debug(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_DEBUG, message);
    }

    public void error(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_ERROR, message);
    }

    public void error(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_ERROR, message, throwable);
    }

    public void info(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_INFO, message);
    }

    public void warn(String pattern, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_WARNING, message);
    }

    public void warn(String pattern, Throwable throwable, Object... args) {
        String message = MessageFormat.format(pattern, args);
        this.log.log(LogService.LOG_WARNING, message, throwable);
    }

}
