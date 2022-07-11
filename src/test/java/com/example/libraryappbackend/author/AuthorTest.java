package com.example.libraryappbackend.author;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    @Test
    public void returnsCorrectlyGeneratedFullname(){
        Author author = new Author("Morgan", "Freeman");

        assertEquals("Morgan Freeman", author.getFullName());
    }
}