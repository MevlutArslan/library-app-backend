package com.example.libraryappbackend.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorName(String authorName);

    List<Book> findByTitle(String title);
}
