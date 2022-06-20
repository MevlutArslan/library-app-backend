package com.example.libraryappbackend.exceptions;

import com.example.libraryappbackend.book.Book;

public class BookNotAvailableException extends Exception {

    public BookNotAvailableException(Book book){
        super("The book with title : " + book.getTitle() + " and author : " + book.getAuthor() + " is not available!");
    }
}
