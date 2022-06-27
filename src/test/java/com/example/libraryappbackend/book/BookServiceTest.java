package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.exceptions.BookDoesntExistException;
import com.example.libraryappbackend.exceptions.BookIsAlreadyInDatabaseException;
import com.example.libraryappbackend.exceptions.BookNotAvailableException;
import com.example.libraryappbackend.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void canGetListOfAllBooks() {

        bookService.getListOfBooks();

        verify(bookRepository).findAll();
    }

    @Test
    void canGetBookById() {
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.getBookById(book.getId());

        verify(bookRepository).findById(book.getId());
    }

    @Test
    void canGetListOfBooksByTitle() {
        String title = "Fault";

        bookService.getListOfBooksByTitle(title);

        verify(bookRepository).findByTitle(title);
    }

    @Test
    void hasUpdatedTitleOfBook() throws BookDoesntExistException {
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        bookService.updateTitleOfBook(book.getId(), "Maze Runner");

        assertEquals("Maze Runner", book.getTitle());
    }

    @Test
    void shouldRentBook() throws BookNotAvailableException {
        // set up
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");
        Users user = new Users("Mevlut", "Arslan", "123456");
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.rentBook(book.getId(), user);

        assertTrue(user.getBooksUnderPossession().contains(book));
        assertEquals(user, book.getCurrentlyWith());
        assertEquals(BookStatus.OCCUPIED, book.getStatus());
    }

    @Test
    void shouldNotRentBook() {
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");
        Users user = new Users("Mevlut", "Arslan", "123456");
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        book.setStatus(BookStatus.OCCUPIED);

        Exception exception = assertThrows(BookNotAvailableException.class, () -> {
            bookService.rentBook(book.getId(), user);
        });

        String expectedMessage = book.getTitle();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldReturnBook() throws BookIsAlreadyInDatabaseException {
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");
        Users user = new Users("Mevlut", "Arslan", "123456");
        book.setCurrentlyWith(user);

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.returnBook(book.getId());

        assertFalse(user.getBooksUnderPossession().contains(book));
        assertNull(book.getCurrentlyWith());
        assertEquals(BookStatus.AVAILABLE, book.getStatus());
    }

    @Test
    void shouldNotReturnBookWhenBookIsAlreadyReturned() throws BookIsAlreadyInDatabaseException {
        Book book = new Book("Percy jackson", new Author("John", "Doe"), "January 1");

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookIsAlreadyInDatabaseException.class, () -> {
            bookService.returnBook(book.getId());
        });

        String expectedMessage = book.getTitle();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldAddNewBook() {
        Book book = new Book();

        bookService.addNewBook(book);
        verify(bookRepository).save(book);
    }


    @Test
    void shouldDeleteBook() {
        Book book = new Book();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.deleteBook(book.getId());
        verify(bookRepository).delete(book);
    }

    @Test
    void shouldNotDeleteBook() {
        // TODO
    }
}