package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-mm-dd")
    private Date birthday;

    @Column(nullable = false)
    private String nationalIdentificationNumber;

    @OneToMany()
    @JoinColumn(name = "book_id")
    @JsonManagedReference("books_under_possession")
    private List<Book> booksUnderPossession = new ArrayList<>();

    @Column()
    private String phoneNumber;

    public Users(String name, String surname, String nationalIdentificationNumber){
        this.name = name;
        this.surname = surname;
        this.nationalIdentificationNumber = nationalIdentificationNumber;
    }

    public void registerBook(Book book){
        booksUnderPossession.add(book);
        book.setIsWith(this);
    }

    public void returnBook(Book book){
        booksUnderPossession.remove(book);
        book.setIsWith(null);
    }

}
