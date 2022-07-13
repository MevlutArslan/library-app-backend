package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Author's name cannot be left blank!")
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
        book.getAuthors().add(this);
        this.booksWritten.add(book);
    }

    public void unregisterBook(Book book){
        book.getAuthors().remove(this);
        this.booksWritten.remove(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return name.equals(author.name) && surname.equals(author.surname) && booksWritten.equals(author.booksWritten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname);
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
