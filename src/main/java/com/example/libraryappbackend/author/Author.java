package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "booksWritten"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Book> booksWritten = new ArrayList<>();

    public Author(String name, String surname){
        this.name = name;
        this.surname = surname;
    }

    public String getFullName(){
        return this.name + " " + this.surname;
    }
}
