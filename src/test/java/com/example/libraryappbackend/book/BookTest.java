package com.example.libraryappbackend.book;

import com.example.libraryappbackend.user.Users;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Calendar;
import java.sql.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {

    static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldCreateBookThatAbidesToConstraints(){
        Book book = new Book(
                "Percy Jackson",
                new Date(2001, Calendar.FEBRUARY, 15)
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void shouldNotCreateBookWithBlankTitle(){
        Book book = new Book(
                "   ",
                new Date(2001, Calendar.FEBRUARY, 15)
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void shouldNotCreateBookWithNullPublishDate(){
        Book book = new Book(
                "Percy Jackson",
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        assertThat(violations.size()).isEqualTo(1);
    }


}