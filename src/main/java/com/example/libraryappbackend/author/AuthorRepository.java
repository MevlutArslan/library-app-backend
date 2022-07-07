package com.example.libraryappbackend.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // TODOr
//    @Query()
//    Author findAuthorByBookTitle(String title);
}
