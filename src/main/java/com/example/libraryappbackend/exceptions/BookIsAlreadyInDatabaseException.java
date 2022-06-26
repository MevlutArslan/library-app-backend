package com.example.libraryappbackend.exceptions;

import com.example.libraryappbackend.book.Book;

public class BookIsAlreadyInDatabaseException extends Exception {

    public BookIsAlreadyInDatabaseException(Book book){
        super("The book with title : " + book.getTitle() + " and author : " + book.getAuthor() + " is already in the database!");
    }
}
