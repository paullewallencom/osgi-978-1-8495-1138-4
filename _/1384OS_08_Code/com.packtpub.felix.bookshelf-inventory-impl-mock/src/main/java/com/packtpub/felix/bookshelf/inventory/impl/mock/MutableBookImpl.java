package com.packtpub.felix.bookshelf.inventory.impl.mock;

import com.packtpub.felix.bookshelf.inventory.api.MutableBook;

public class MutableBookImpl implements MutableBook
{
    String isbn;

    String author;
    String title;
    String group;

    int grade;

    transient String _toString = null;
    
    public MutableBookImpl(String isbn) {
        setISBN(isbn);
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
        _toString = null;
    }

    public String getISBN() {
        return this.isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
        _toString = null;
    }

    public String getAuthor() {
        return this.author;
    }
    
    public void setTitle(String title) {
        this.title = title;
        _toString = null;
    }


    public String getTitle() {
        return this.title;
    }

    public void setGroup(String group) {
        this.group = group;
        _toString = null;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGrade(int grade) {
        this.grade = grade;
        _toString = null;
    }

    public int getGrade() {
        return this.grade;
    }
    
    public String toString() {
        if (_toString==null) {
            StringBuffer buf = new StringBuffer();
            buf.append(getGroup()).append(": ");
            buf.append(getTitle()).append(" from ").append(getAuthor());
            buf.append(" (").append(getISBN()).append(") ");
            buf.append(" [").append(getGrade()).append("/10]");
            this._toString = buf.toString();
        }
        return _toString;
    }
}
