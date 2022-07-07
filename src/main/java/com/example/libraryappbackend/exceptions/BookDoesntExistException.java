package com.example.libraryappbackend.exceptions;

public class BookDoesntExistException extends Exception{
    public BookDoesntExistException(Long id){
        super("Book with id : " + id + " does not exist in our system");
    }
}
