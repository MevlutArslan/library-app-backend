package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    private AuthorService authorService;

    @BeforeEach
    void setUp(){
        authorService = new AuthorService(authorRepository, bookRepository);
    }

    @Test
    void shouldGetListOfAuthors() {
        verify(authorRepository).findAll();
    }

    @Test
    void shouldCreateAuthor(){
        Author author = new Author("Morgan", "Freeman");

        verify(authorRepository).save(author);
    }

    @Test
    void shouldNotCreateAuthorThatAlreadyExists(){
        Author author = new Author("Morgan", "Freeman");
        authorRepository.save(author);

        when(authorRepository.findAuthorByName(author.getName())).thenReturn(author);

        Author duplicateAuthor = new Author("Morgan", "Freeman");

        Exception exception = assertThrows(AlreadyExistsException.class, () -> {
            authorService.createAuthor(duplicateAuthor);
        });

        String expectedMessage = duplicateAuthor.getName();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void shouldGetAuthorById() {
        Author author = new Author("Morgan", "Freeman");

        verify(authorRepository).findById(author.getId());
    }

    @Test
    void shouldGetBooksWrittenByAuthorWithId() {
        Book book = new Book("Percy Jackson", new Date(1999,06,11));
        Author author = new Author("Morgan", "Freeman");

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        author.registerBook(book);

        Set<Book> booksWritten = authorService.getBooksWrittenByAuthorWithId(author.getId());

        assertTrue(author.getBooksWritten().containsAll(booksWritten));
    }

    @Test
    void shouldUpdateAuthorInformation() {
        Author author = new Author("Morgan", "Freeman");

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Author updatedAuthor = new Author("Morgan", "Stanley");

        authorService.updateAuthorInformation(author.getId(), updatedAuthor);

        assertEquals(updatedAuthor.getSurname(), author.getSurname());
    }

    @Test
    void shouldDeleteAuthor() {
        Author author = new Author("Morgan", "Freeman");

        authorService.deleteAuthor(author.getId());

        verify(authorRepository).delete(author);
    }

    @Test
    void shouldRegisterBookToAuthor(){
        Author author = new Author("Morgan", "Freeman");
        Book book = new Book("Percy Jackson", new Date(1999,6,11));

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        authorService.registerBookToAuthor(author.getId(), book.getId());

        assertTrue(book.getAuthors().contains(author));
        assertTrue(author.getBooksWritten().contains(book));
    }

    @Test
    void shouldUnregisterBookFromAuthor(){
        Author author = new Author("Morgan", "Freeman");
        Book book = new Book("Percy Jackson", new Date(1999,6,11));

        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        authorService.removeBookFromAuthor(author.getId(), book.getId());

        assertFalse(book.getAuthors().contains(author));
        assertFalse(author.getBooksWritten().contains(book));
    }
}