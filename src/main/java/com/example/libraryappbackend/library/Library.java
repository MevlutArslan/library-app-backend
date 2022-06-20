package com.example.libraryappbackend.library;

import com.example.libraryappbackend.admin.Admin;
import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Collection;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany()
    private Collection<Admin> admins;

    @OneToMany
    private Collection<User> users;

    @OneToMany
    private Collection<Book> books;

}
