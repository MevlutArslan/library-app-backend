package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.BookIsAlreadyInDatabaseException;
import com.example.libraryappbackend.exceptions.BookNotAvailableException;
import com.example.libraryappbackend.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getListOfBooks() {
        return this.bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return this.bookRepository.findById(id);
    }

    public List<Book> getListOfBooksByTitle(String title) {
        return this.bookRepository.findByTitle(title);
    }

    public void updateTitleOfBook(Long id, String title) {
        Book toUpdate = this.bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
        toUpdate.setTitle(title);
    }

    public void rentBook(Long id, Users user) throws BookNotAvailableException {
        Book book = this.bookRepository.findById(id).orElseThrow(NoSuchElementException::new);

        // TODO :Don't really remember how to use custom exceptions... Will need to look here again
        if (book.getStatus().equals(BookStatus.AVAILABLE)) {
            user.getBooksUnderPossession().add(book);
            book.setCurrentlyWith(user);
            book.setStatus(BookStatus.OCCUPIED);
        } else
            throw new BookNotAvailableException(book);

    }

    // TODO : Rewrite this method its very stupid and ugly
    public void returnBook(Long id) throws BookIsAlreadyInDatabaseException {
        Book book = this.bookRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if (book.getCurrentlyWith() == null) {
            throw new BookIsAlreadyInDatabaseException(book);
        } else {
            book.getCurrentlyWith().getBooksUnderPossession().remove(book);
            book.setCurrentlyWith(null);
            book.setStatus(BookStatus.AVAILABLE);
        }
    }

    public void addNewBook(Book book) {
        this.bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> toDelete = this.bookRepository.findById(id);
        toDelete.ifPresent((book) -> {
            this.bookRepository.delete(book);
        });
    }

}
