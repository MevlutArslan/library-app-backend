package com.example.libraryappbackend.exceptions;

public class AlreadyExistsException extends RuntimeException{

    public <T>AlreadyExistsException(T object){
        super(object + " , already exists!");
    }


}
