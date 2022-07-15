package com.example.libraryappbackend.exceptions;

import com.example.libraryappbackend.user.Users;

public class AlreadyExistsException extends RuntimeException{

    public <T>AlreadyExistsException(T object){
        super("Object with same field exists in the database!");
    }
}
