package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    private UsersService usersService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp(){
        usersService = new UsersService(usersRepository, bookRepository);
    }

    @Test
    void shouldGetUserById() {
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        usersService.getUserById(user.getId());

        verify(usersRepository).findById(user.getId());
    }

    @Test
    void shouldGetListOfAllUsers() {
        usersService.getListOfAllUsers();

        verify(usersRepository).findAll();
    }

    @Test
    void shouldGetListOfAllBooksUnderPossession() {
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));

        Book book_1 = new Book("Percy jackson", new Date(2020, 1, 11));
        Book book_2 = new Book("Poseidon",  new Date(2019, 11, 15));

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        user.getBooksUnderPossession().add(book_1);
        user.getBooksUnderPossession().add(book_2);

        assertTrue(user.getBooksUnderPossession().contains(book_1));
    }

    @Test
    void shouldSaveUser() throws AlreadyExistsException {
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));

        usersService.saveUser(user);

        verify(usersRepository).save(user);
    }

    @Test
    void shouldUpdateUser(){
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));

        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Users updatedUser = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));
        usersService.updateUser(user.getId(), updatedUser);

        assertEquals(user, updatedUser);
    }

    @Test
    void shouldRegisterBookToUser(){
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));
        Book book = new Book("Percy Jackson",new Date(1999,11,12));

        long userId = 10L;
        long bookId = 12L;

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        usersService.registerBookToUser(Long.toString(userId), Long.toString(bookId));

        assertTrue(user.getBooksUnderPossession().contains(book));
        assertEquals(user, book.getIsWith());
    }

    @Test
    void shouldReturnBookFromUser(){
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));
        Book book = new Book("Percy Jackson",new Date(1999,11,12));

        long userId = 10L;
        long bookId = 12L;

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        usersService.returnBookFromUser(Long.toString(userId), Long.toString(bookId));

        assertFalse(user.getBooksUnderPossession().contains(book));
        assertNotEquals(user, book.getIsWith());
    }

    @Test
    void shouldDeleteUser() {
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));
        when(usersRepository.findById(user.getId())).thenReturn(Optional.of(user));

        this.usersService.deleteUser(user.getId());

        verify(usersRepository).delete(user);
    }
}