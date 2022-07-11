package com.example.libraryappbackend.utility;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

//    @Bean
//    CommandLineRunner initDatabase(BookRepository bookRepository) {
//        List<Book> bookList = new ArrayList<Book>();
//
//        Author author_1 = new Author("Jack", "Black");
//        Author author_2 = new Author("Jordan", "Pele");
//        Author author_3 = new Author("Tony", "Stark");
//
//        bookList.add(new Book("Percy Jackson", author_1, "2nd of January"));
//        bookList.add(new Book("Venom", author_2, "2nd of June"));
//        bookList.add(new Book("Spider-Man", author_3, "11th of June"));
//        bookList.add(new Book("Scooby Doo", author_1, "11th of July"));
//        bookList.add(new Book("Adventures of Tintin", author_2, "1st of September"));
//        bookList.add(new Book("Winning Time", author_3, "2nd of January"));
//        bookList.add(new Book("12 Rules", author_3, "2nd of January"));
//        bookList.add(new Book("Honey Trap", author_1, "2nd of January"));
//        bookList.add(new Book("Amoung You", author_2, "2nd of January"));
//        bookList.add(new Book("Corrupt Politicians", author_1, "15th of July"));
//
//        return (args) -> {
//            for(int i = 0; i < bookList.size(); i++){
//                Book book = bookList.get(i);
//                log.info("Preloading : " + bookRepository.save(book));
//            }
//        };
//    }
}
