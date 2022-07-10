package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String surname;

    @ManyToMany(mappedBy = "authors")
    @JsonIgnore
    private Set<Book> booksWritten = new HashSet<>();

    public Author(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public String getFullName(){
        return this.name + " " + this.surname;
    }

    public void registerBook(Book book){
        this.booksWritten.add(book);
        book.getAuthors().add(this);
    }

    public void unregisterBook(Book book){
        this.booksWritten.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return id.equals(author.id) && name.equals(author.name) && surname.equals(author.surname) && booksWritten.equals(author.booksWritten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }
}
