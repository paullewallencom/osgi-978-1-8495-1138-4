package com.packtpub.felix.bookshelf.inventory.api;

/** Holds information on a book in the bookshelf. */
public interface Book {
	
	/** Get the book reference number (ISBN) */
	String getISBN();
	
	/** Get the book title */
	String getTitle();
	
	/** Get the book author */
	String getAuthor();
	
	/** Get the book group */
	String getGroup();
	
	/** Get the awarded grade (0 to 10) */
	int getGrade();
}
