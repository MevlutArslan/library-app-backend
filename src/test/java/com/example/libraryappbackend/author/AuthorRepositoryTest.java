package com.example.libraryappbackend.author;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @AfterEach
    void tearDown(){}

    @Test
    void shouldFindAuthorByName() {
        Author author = new Author("Morgan", "Freeman");
        authorRepository.save(author);

        assertEquals(author, authorRepository.findAuthorByName(author.getName()));
    }
}