package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsersService {

    private UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Users getUserById(Long id){
        return this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Users> getAllUsers(){
        return this.usersRepository.findAll();
    }

    public void createUser(Users user){
        this.usersRepository.save(user);
    }

    public void updateBirthday(Long id, Date newBirthdayDate){
        Users userToUpdate = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userToUpdate.setBirthday(newBirthdayDate);
    }

    public void updateNationalIdentificationNumber(Long id, String newNationalIdentificationNumber){
        Users userToUpdate = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userToUpdate.setNationalIdentificationNumber(newNationalIdentificationNumber);
    }

    public void updatePhoneNumber(Long id, String newPhoneNumber){
        Users userToUpdate = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        userToUpdate.setPhoneNumber(newPhoneNumber);
    }

    public void deleteUser(Long id){
        Users user = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        this.usersRepository.delete(user);
    }

    public List<Book> getListOfAllBooksUnderPossession(Long userId){
        Users user = this.usersRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return user.getBooksUnderPossession();
    }

    // TODO : Maybe handle assigning & returning books in here instead of the Book's service

}
