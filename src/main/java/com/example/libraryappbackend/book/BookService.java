package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.BookIsAlreadyInDatabaseException;
import com.example.libraryappbackend.exceptions.BookNotAvailableException;
import com.example.libraryappbackend.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Book getBookById(Long id) {
        return this.bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    // TODO : Can handle this on the front-end not sure if I should do it here
//    public List<Book> getListOfBooksByTitle(String title) {
//        return this.bookRepository.findByTitle(title);
//    }

    @Transactional
    public void updateBook(Long bookID, Book book) {
        Book toUpdate = this.bookRepository.findById(bookID).orElseThrow(NoSuchElementException::new);

        toUpdate.setTitle(book.getTitle());
        toUpdate.setAuthor(book.getAuthor());
        toUpdate.setPublishedDate(book.getPublishedDate());
        toUpdate.setStatus(book.getStatus());
        toUpdate.setIsWith(book.getIsWith());
    }

    public void addNewBook(Book book) {
        // TODO implement logic to make sure we don't have duplicates in the db
        this.bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> toDelete = this.bookRepository.findById(id);
        toDelete.ifPresent((book) -> {
            this.bookRepository.delete(book);
        });
    }

}
