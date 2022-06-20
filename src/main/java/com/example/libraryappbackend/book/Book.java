package com.example.libraryappbackend.book;

import com.example.libraryappbackend.user.User;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
public class Book {

    @Id
    @GeneratedValue
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
    @Column
    private User currentlyWith;
}
