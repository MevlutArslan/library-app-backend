package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import lombok.Getter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    // TODO : Change all dates to a proper format
    @Column(nullable = false)
    private String birthday;

    @Column(nullable = false)
    private String nationalIdentificationNumber;

    @OneToMany
    private Collection<Book> booksUnderPossession;

    private String phoneNumber;

}
