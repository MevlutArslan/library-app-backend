package com.example.libraryappbackend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByNationalIdentificationNumber(String nationalIdentificationNumber);
}
