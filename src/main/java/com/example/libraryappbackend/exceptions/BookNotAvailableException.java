package com.example.libraryappbackend.exceptions;

public class BookNotAvailableException extends RuntimeException{

    public BookNotAvailableException(Long id){
        super("Book with id : " + id + " not available!");
    }
}
