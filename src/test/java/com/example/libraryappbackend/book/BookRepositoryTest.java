package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown(){
        bookRepository.deleteAll();
    }

    @Test
    void findByTitle() {
        Book book = new Book("Percy jackson", new Date(2020, 1, 11));
        bookRepository.save(book);

        List<Book> booksWithTitle = bookRepository.findByTitle(book.getTitle());

        assertThat(booksWithTitle.contains(book)).isTrue();
    }
}