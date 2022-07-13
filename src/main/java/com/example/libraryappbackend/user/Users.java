package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookStatus;
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

    @Column(nullable = false, unique = true)
    private String nationalIdentificationNumber;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-mm-dd")
    private Date birthday;

    @Column
    private String phoneNumber;

    @OneToMany(mappedBy = "isWith")
    @JsonManagedReference("books_under_possession")
    private List<Book> booksUnderPossession = new ArrayList<>();

    public Users(String name, String surname, String nationalIdentificationNumber, Date birthday){
        this.name = name;
        this.surname = surname;
        this.nationalIdentificationNumber = nationalIdentificationNumber;
        this.birthday = birthday;
    }

    public void registerBook(Book book){
        booksUnderPossession.add(book);
        book.setIsWith(this);
        book.setStatus(BookStatus.OCCUPIED);
    }

    public void returnBook(Book book){
        booksUnderPossession.remove(book);
        book.setIsWith(null);
        book.setStatus(BookStatus.AVAILABLE);
    }
}
