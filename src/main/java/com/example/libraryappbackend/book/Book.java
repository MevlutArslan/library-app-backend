package com.example.libraryappbackend.book;

import com.example.libraryappbackend.user.Users;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publishedDate;

    @Column(nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @OneToOne
    private Users currentlyWith;
}
