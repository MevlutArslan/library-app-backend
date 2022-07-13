package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.user.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // TODO change to proper format
    @Column(nullable = false)
    private Date publishedDate;

    @Column(nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "books_by_author",
               joinColumns = @JoinColumn(name = "book_id"),
               inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("books_under_possession")
    private Users isWith;

    public Book(String title, Date publishedDate){
        this.title = title;
        this.publishedDate = publishedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return title.equals(book.title) && publishedDate.equals(book.publishedDate) && status == book.status && authors.equals(book.authors) && Objects.equals(isWith, book.isWith);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publishedDate, status, authors, isWith);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", publishedDate=" + publishedDate +
                ", status=" + status +
                ", authors=" + authors +
                ", isWith=" + isWith +
                '}';
    }
}