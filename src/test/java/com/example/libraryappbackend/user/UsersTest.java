package com.example.libraryappbackend.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UsersTest {

    static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Test
    void shouldCreateUserThatAbidesToConstraints(){
        Users user = new Users(
                "Mevlut",
                "Arslan",
                "U29202020",
                new Date(1999, Calendar.JUNE,11)
        );

        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void shouldNotAllowBlankName(){
        Users user = new Users();
        user.setSurname("Arslan");
        user.setPhoneNumber("999999999");
        user.setBirthday(new Date(1999, Calendar.JUNE,11));
        user.setNationalIdentificationNumber("U29202020");

        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void shouldNotAllowBlankSurname(){
        Users user = new Users();
        user.setName("Mevlut");
        user.setSurname("   ");
        user.setPhoneNumber("999999999");
        user.setBirthday(new Date(1999, Calendar.JUNE,11));
        user.setNationalIdentificationNumber("U29202020");

        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void shouldNotAllowNullBirthday(){
        Users user = new Users();
        user.setName("Mevlut");
        user.setSurname("Arslan");
        user.setBirthday(null);
        user.setNationalIdentificationNumber("U29202020");

        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void shouldNotAllowBlankNationalID(){
        Users user = new Users();
        user.setName("Mevlut");
        user.setSurname("Arslan");
        user.setNationalIdentificationNumber("   ");
        user.setBirthday(new Date(1999, Calendar.JUNE,11));


        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        // there are 2 violations here, one for the blank entry and another for not fulfilling the length requirement
        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    void shouldNotAllowPhoneNumberShorterThanNineDigits(){
        Users user = new Users();
        user.setName("Mevlut");
        user.setSurname("Arslan");
        user.setNationalIdentificationNumber("U29202020");
        user.setBirthday(new Date(1999, Calendar.JUNE,11));


        user.setPhoneNumber("345678");

        Set<ConstraintViolation<Users>> violations = validator.validate(user);

        assertThat(violations.size()).isEqualTo(1);
    }
}