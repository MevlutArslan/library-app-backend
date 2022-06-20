package com.example.libraryappbackend.admin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Admin {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    private String nationalIdentificationNumber;

    // TODO : Implement Profile Picture
//    private Ima

}
