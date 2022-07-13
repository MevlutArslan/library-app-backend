package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock //Because we have tested our BookRepository we do not need to load in the entire database to test the service
    // We simply know that the repository works and all the methods of it will return accurate results so we do not need to check
    // our query's results we just need to make sure the expected actions were taken within the method we are testing.
    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp(){
        bookService = new BookService(bookRepository);
    }

    @Test
    void shouldGetListOfAllBooks() {

        bookService.getListOfBooks();

        verify(bookRepository).findAll();
    }

    @Test
    void shouldGetBookById() {
        Book book = new Book("Percy jackson", new Date(2020, 1, 11));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.getBookById(book.getId());

        verify(bookRepository).findById(book.getId());
    }


    @Test
    void shouldAddNewBook() throws AlreadyExistsException {
        Book book = new Book();

        bookService.addNewBook(book);
        verify(bookRepository).save(book);
    }

    @Test
    void shouldUpdateBook(){
        Book book = new Book("Percy jackson", new Date(2020, 1, 11));

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        Book newBook = new Book("Percy Jackson : The lightning thief", book.getPublishedDate());

        this.bookService.updateBook(book.getId(), newBook);

        assertEquals(newBook, book);
    }


    @Test
    void shouldDeleteBook() {
        Book book = new Book();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.deleteBook(book.getId());
        verify(bookRepository).delete(book);
    }
}