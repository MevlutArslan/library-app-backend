package com.example.libraryappbackend.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;
import static java.util.Calendar.JULY;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
//  This annotation creates an application context and loads the full application context.
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_ROUTE = ROOT_API_ROUTE + "/books";

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(bookRepository).isNotNull();
    }

    @Test
    void shouldGetBookById() throws Exception {
        Book book = new Book(
                "Percy Jackson",
                new Date(1995, JULY, 12)
        );

        bookRepository.save(book);

        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", book.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value(book.getTitle()));
    }

    @Test
    void shouldNotGetBookById_WhenIdDoesNotExist() throws Exception {
        ResultActions response = mockMvc.perform(
                get(API_ROUTE + "/{id}", "1"));

        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldGetListOfAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(
                "Percy Jackson",
                new Date(1995, JULY, 12)
        ));
        books.add(new Book(
                "Bill Gates",
                new Date(1995, JULY, 12)
        ));
        books.add(new Book(
                "Obama",
                new Date(1995, JULY, 12)
        ));
        books.add(new Book(
                "Trump",
                new Date(1995, JULY, 12)
        ));

        bookRepository.saveAll(books);

        ResultActions response = mockMvc.perform(get(API_ROUTE));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(books.size()));
    }

    @Test
    void shouldAddBook() throws Exception {
        // given
        Book book = new Book(
                "Percy Jackson",
                new Date(1995, JULY, 12)
        );

        // when
        ResultActions response = mockMvc.perform(post(API_ROUTE).contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(book)));

        // then
        response.andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateBook() throws Exception {
        Book book = new Book(
                "Percy Jackson",
                new Date(1995, JULY, 12)
        );

        Book updatedBook = new Book(
                "Micheal Jackson",
                new Date(1995, JULY, 12)
        );

        bookRepository.save(book);

        ResultActions response = mockMvc.perform(put(API_ROUTE + "/{id}", book.getId())
                .contentType(APPLICATION_JSON).content(objectMapper.writeValueAsBytes(updatedBook)));

        response.andExpect(status().isOk())
                .andDo(print());

        assertEquals(bookRepository.findById(book.getId()).get().getTitle(),
                     updatedBook.getTitle());
    }

    @Test
    void shouldDeleteBook() throws Exception{
        Book book = new Book(
                "Percy Jackson",
                new Date(1995, JULY, 12)
        );

        bookRepository.save(book);

        ResultActions response = mockMvc.perform(delete(API_ROUTE + "/{id}", book.getId()));

        response.andExpect(status().isOk());

        assertEquals(0, bookRepository.findByTitle(book.getTitle()).size());
    }
}