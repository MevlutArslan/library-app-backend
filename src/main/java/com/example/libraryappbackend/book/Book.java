package com.example.libraryappbackend.book;

import com.example.libraryappbackend.author.Author;
import com.example.libraryappbackend.user.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    /*
    As a rule of thumb, we should prefer the @NotNull annotation over the @Column(nullable = false) annotation.
    This way, we make sure the validation takes place before Hibernate sends any insert or update SQL queries to the database.

    Also, it's usually better to rely on the standard rules defined in the Bean Validation,
    rather than letting the database handle the validation logic.
     */
    @Column
    @NotBlank(message = "title cannot be empty!")
    private String title;

    @Column
    @NotNull(message = "published date cannot be empty!")
    private Date publishedDate;

    @Column
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