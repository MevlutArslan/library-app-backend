package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    void shouldFindBooksByAuthor() {
        Author author = new Author("Morgan", "Freeman");

        Book book = new Book("exampleTitle", author,"2nd of June");
        bookRepository.save(book);

        List<Book> booksByAuthor = bookRepository.findByAuthorName(book.getAuthor().getName());

        assertThat(booksByAuthor.contains(book)).isTrue();
    }

    @Test
    void findByTitle() {
        Author author = new Author("Morgan", "Freeman");

        Book book = new Book("Lucy", author,"2nd of June");
        bookRepository.save(book);

        List<Book> booksWithTitle = bookRepository.findByTitle(book.getTitle());

        assertThat(booksWithTitle.contains(book)).isTrue();
    }
}