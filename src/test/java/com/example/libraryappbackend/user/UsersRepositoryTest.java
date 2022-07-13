package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;


    @AfterEach
    void tearDown(){
        usersRepository.deleteAll();
    }

    @Test
    void shouldFindByNationalID() {
        Users user = new Users("Mevlut", "Arslan", "U20229292", new Date(1999, 6, 11));
        usersRepository.save(user);

        Users userFound = usersRepository.findByNationalIdentificationNumber(user.getNationalIdentificationNumber()).get();

        assertEquals(user, userFound);
    }
}