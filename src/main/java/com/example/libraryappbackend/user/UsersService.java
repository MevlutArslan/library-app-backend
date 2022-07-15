package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.example.libraryappbackend.book.BookStatus;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import com.example.libraryappbackend.exceptions.BookNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository, BookRepository bookRepository){
        this.usersRepository = usersRepository;
        this.bookRepository = bookRepository;
    }

    public Users getUserById(Long id){
        return this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Users> getListOfAllUsers(){
        return this.usersRepository.findAll();
    }

    public void saveUser(Users user) throws AlreadyExistsException {
        Optional<Users> existingUser = usersRepository.findByNationalIdentificationNumber(user.getNationalIdentificationNumber());

        if(existingUser.isEmpty()){
            this.usersRepository.save(user);
        }else{
            throw new AlreadyExistsException(user);
        }

    }

    @Transactional // Allows the use of setters to update fields instead of writing querys.
    public void updateUser(Long id, Users newUser){
        Users user = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setNationalIdentificationNumber(newUser.getNationalIdentificationNumber());
        user.setBirthday(newUser.getBirthday());
        user.setPhoneNumber(newUser.getPhoneNumber());
    }

    public void deleteUser(Long id){
        Users user = this.usersRepository.findById(id).orElseThrow(NoSuchElementException::new);
        this.usersRepository.delete(user);
    }

    public List<Book> getListOfAllBooksUnderPossession(Long userId){
        Users user = this.usersRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return user.getBooksUnderPossession();
    }

    @Transactional
    public void registerBookToUser(String userID, String bookID) {
        Users user = this.usersRepository.findById(Long.parseLong(userID))
                .orElseThrow(NoSuchElementException::new);
        Book book = bookRepository.findById(Long.parseLong(bookID)).orElseThrow(NoSuchElementException::new);
        if(book.getStatus().equals(BookStatus.OCCUPIED)){
            throw new BookNotAvailableException(book.getId());
        }
        user.registerBook(book);
    }

    @Transactional
    public void returnBookFromUser(String userID, String bookID) {
        Users user = this.usersRepository.findById(Long.parseLong(userID))
                .orElseThrow(NoSuchElementException::new);
        Book book = bookRepository.findById(Long.parseLong(bookID)).orElseThrow(NoSuchElementException::new);
        user.returnBook(book);
    }
}
