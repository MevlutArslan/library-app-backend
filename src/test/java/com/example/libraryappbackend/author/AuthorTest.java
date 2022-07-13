package com.example.libraryappbackend.author;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void returnsCorrectlyGeneratedFullname(){
        Author author = new Author("Morgan", "Freeman");

        assertEquals("Morgan Freeman", author.getFullName());
    }

    @Test
    void shouldCreateAuthorThatAbidesToConstraints(){
        Author author = new Author("Morgan", "Freeman");
        author.setEmail("morgan_freeman@gmail.com");

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void shouldNotCreateAuthorWithEmptyName(){
        Author author = new Author("", "Freeman");
        author.setEmail("morgan_freeman@gmail.com");

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void shouldNotCreateAuthorWithFalseEmailFormat(){
        Author author = new Author("Morgan", "Freeman");
        author.setEmail("morgan_freeman_gmail.com");

        Set<ConstraintViolation<Author>> violations = validator.validate(author);

        assertThat(violations.size()).isEqualTo(1);
    }

}