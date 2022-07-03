package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.user.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    // TODO change to proper format
    @Column(nullable = false)
    private Date publishedDate;

    @Column(nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("books_under_possession")
    private Users isWith;


    public Book(String title, Author author, Date publishedDate) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
    }

}
