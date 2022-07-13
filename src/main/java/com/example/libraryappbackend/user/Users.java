package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @Column
    @NotBlank(message = "User's name cannot be left blank!")
    private String name;

    @Column
    @NotBlank(message = "User's surname cannot be left blank!")
    private String surname;

    @Column(unique = true)
    @NotBlank(message = "National Identification Number cannot be left blank!")
    @Length(min = 9, max = 9, message = "A national identification number should be of length 9!") // "A valid National Identification number consists of one letter and nine-digits" - Wikipedia
    private String nationalIdentificationNumber;

    @Column
    @JsonFormat(pattern="yyyy-mm-dd")
    @NotNull(message = "Birthday cannot be null!")
    private Date birthday;

    @Column(unique = true)
    @Length(min = 9, message = "Your phone number should consist of atleast 9 digits")
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
