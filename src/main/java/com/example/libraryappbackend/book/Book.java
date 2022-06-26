package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.user.Users;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

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
    private String publishedDate;

    @Column(nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @OneToOne
    private Users currentlyWith = null;

    public Book(String title, Author author, String publishedDate){
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
    }
}
