package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.example.libraryappbackend.book.BookStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;
import static java.util.Calendar.JUNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class UsersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookRepository bookRepository;

    private final String NAME = "Mevlut";
    private final String SURNAME = "Arslan";
    private final String NATIONAL_ID = "U29202020";
    private final Date BIRTHDAY = new Date(1999, JUNE, 11);

    private final String API_ROUTE = ROOT_API_ROUTE + "/users";

    @BeforeEach
    void setUp() {
        usersRepository.deleteAll();
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(usersRepository).isNotNull();
    }

    @Test
    void shouldGetUserById() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);

        usersRepository.save(user);

        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", user.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.surname").value(user.getSurname()))
                .andExpect(jsonPath("$.nationalIdentificationNumber").value(user.getNationalIdentificationNumber()));

    }

    @Test
    void shouldNotGetUserById_WhenIdDoesNotExist() throws Exception {
        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", "1"));

        response.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    void shouldGetListOfAllUsers() throws Exception {
        List<Users> users = new ArrayList<>();

        users.add(new Users(
                "Mustafa",
                "Arslan",
                "U22233344",
                new Date()
        ));

        users.add(new Users(
                "Sevda",
                "Arslan",
                "U33344455",
                new Date()
        ));

        usersRepository.saveAll(users);

        ResultActions response = mockMvc.perform(get(API_ROUTE));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(users.size()));
    }

    @Test
    void shouldAddNewUser() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);

        ResultActions response = mockMvc.perform(post(API_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(user)));

        response.andExpect(status().isCreated()).andDo(print());
    }

    @Test
    void shouldNotAddNewUser_WhenUserAlreadyExists() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        Users duplicateUser = new Users("MUSTAFA", "ARSLAN", NATIONAL_ID, BIRTHDAY);

        ResultActions response = mockMvc.perform(post(API_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(duplicateUser)));

        response.andExpect(status().isConflict()).andDo(print());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        Users updatedUser = new Users("Mustafa", SURNAME, NATIONAL_ID, BIRTHDAY);

        ResultActions response = mockMvc.perform(put(API_ROUTE + "/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedUser)));

        response.andExpect(status().isOk()).andDo(print());

        assertEquals(updatedUser.getName(), usersRepository.findById(user.getId()).get().getName());
    }

    @Test
    @Transactional
    void shouldRegisterBookToUser() throws Exception{
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        Book book = new Book("Percy Jackson", new java.sql.Date(1999,6,11));
        bookRepository.save(book);

        ResultActions response = mockMvc.perform(post(API_ROUTE + "/{id}/registerBook/{bookID}", user.getId(), book.getId()));

        response.andExpect(status().isOk())
                .andDo(print());

        assertEquals(user, book.getIsWith());
        assertTrue(user.getBooksUnderPossession().contains(book));
    }

    @Test
    void shouldNotRegisterOccupiedBookToUser() throws Exception{
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        Book book = new Book("Percy Jackson", new java.sql.Date(1999,6,11));
        book.setStatus(BookStatus.OCCUPIED);
        bookRepository.save(book);

        ResultActions response = mockMvc.perform(post(API_ROUTE + "/{id}/registerBook/{bookID}", user.getId(), book.getId()));

        response.andExpect(status().isNotAcceptable()).andDo(print());
    }

    @Test
    @Transactional
    void shouldReturnBookFromUser() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        Book book = new Book("Percy Jackson", new java.sql.Date(1999,6,11));
        bookRepository.save(book);

        user.registerBook(book);

        ResultActions response = mockMvc.perform(post(API_ROUTE + "/{id}/returnBook/{bookID}", user.getId(), book.getId()));

        response.andExpect(status().isOk())
                .andDo(print());

        assertNotEquals(user, book.getIsWith());
        assertFalse(user.getBooksUnderPossession().contains(book));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        ResultActions response = mockMvc.perform(delete(API_ROUTE + "/{id}", user.getId()));

        response.andExpect(status().isOk());

        assertEquals(Optional.empty(), usersRepository.findByNationalIdentificationNumber(user.getNationalIdentificationNumber()));
    }

    @Test
    @Transactional
    void shouldGetListOfAllBooksUnderPossession() throws Exception {
        Users user = new Users(NAME, SURNAME, NATIONAL_ID, BIRTHDAY);
        usersRepository.save(user);

        final int BOOK_COUNT = 3;

        user.registerBook(new Book("Percy", new java.sql.Date(1999,6,11)));
        user.registerBook(new Book("Jackson", new java.sql.Date(1959,6,11)));
        user.registerBook(new Book("Micheal", new java.sql.Date(2172,6,11)));

        ResultActions response = mockMvc.perform(get(API_ROUTE + "/{id}/under_possesion", user.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(BOOK_COUNT));
    }
}