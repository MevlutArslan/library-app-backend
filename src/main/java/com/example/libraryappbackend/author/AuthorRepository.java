package com.example.libraryappbackend.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // TODO
//    @Query()
//    Author findAuthorByBookTitle(String title);

    Author findAuthorByName(String name);
}
