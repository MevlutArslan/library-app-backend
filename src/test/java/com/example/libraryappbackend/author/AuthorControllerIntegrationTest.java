package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;
import static java.util.Calendar.JULY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
//  This annotation creates an application context and loads the full application context.
@AutoConfigureMockMvc
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    private final String API_ROUTE = ROOT_API_ROUTE + "/authors";


    @BeforeEach
    void setUp() {
        authorRepository.deleteAll();
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(authorRepository).isNotNull();
    }

    @Test
    void shouldGetAuthorById() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");

        authorRepository.save(author);

        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", author.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.fullName").value(author.getFullName()))
                .andExpect(jsonPath("$.email").value(author.getEmail()));
    }

    @Test
    void shouldNotGetAuthorById_WhenAuthorDoesNotExist() throws Exception {
        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", "1"));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldGetListOfAuthors() throws Exception {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Morgan", "Freeman", "morganFreeman@gmail.com"));
        authors.add(new Author("Fetullah", "Gulen"));

        authorRepository.saveAll(authors);

        ResultActions response = mockMvc.perform(get(API_ROUTE));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(authors.size()));
    }

    @Test
    void shouldGetBooksWrittenByAuthor() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        List<Book> books = new ArrayList<>();
        books.add(new Book("Percy Jackson", new Date(1995, JULY, 12)));
        books.add(new Book("Micheal Jackson", new Date(1995, JULY, 12)));

        bookRepository.saveAll(books);

        for (Book book : books) {
            author.registerBook(book);
        }

        ResultActions response = mockMvc.perform(get(API_ROUTE + "/{id}/books"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(books.size()));
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");

        ResultActions response = mockMvc.perform(post(API_ROUTE).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(author)));

        response.andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateAuthor_WhenAuthorAlreadyExists() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        ResultActions response = mockMvc.perform(post(API_ROUTE).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(author)));

        response.andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateAuthorInformation() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        Author updatedAuthor = new Author("Morgan", "Stanley", "morganStanley@gmail.com");

        ResultActions response = mockMvc.perform(put(API_ROUTE + "/{id}", author.getId()).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(updatedAuthor)));

        response.andExpect(status().isOk()).andDo(print());

        Author authorInDB = authorRepository.findById(author.getId()).get();

        assertEquals(updatedAuthor.getSurname(), authorInDB.getSurname());
        assertEquals(updatedAuthor.getEmail(), authorInDB.getEmail());
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        ResultActions response = mockMvc.perform(delete(API_ROUTE + "/{id}", author.getId()));

        Author authorInDB = authorRepository.findAuthorByEmail(author.getEmail());

        response.andExpect(status().isOk()).andDo(print());

        assertNull(authorInDB);
    }

    @Test
    @Transactional
    void shouldRegisterBookToAuthor() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        Book book = new Book("Percy Jackson", new Date(1995, JULY, 12));
        bookRepository.save(book);

        ResultActions response = mockMvc.perform(post(API_ROUTE + "/{id}/registerBookToAuthor/{bookID}",
                author.getId(), book.getId()));

        response.andExpect(status().isOk());

        assertTrue(book.getAuthors().contains(author));
        assertTrue(author.getBooksWritten().contains(book));
    }

    @Test
    @Transactional
    void shouldRemoveBookFromAuthor() throws Exception {
        Author author = new Author("Morgan", "Freeman", "morganFreeman@gmail.com");
        authorRepository.save(author);

        Book book = new Book("Percy Jackson", new Date(1995, JULY, 12));
        bookRepository.save(book);

        ResultActions response = mockMvc.perform(post(API_ROUTE + "/{id}/removeBookFromAuthor/{bookID}",
                author.getId(), book.getId()));

        response.andExpect(status().isOk());

        assertFalse(book.getAuthors().contains(author));
        assertFalse(author.getBooksWritten().contains(book));
    }
}