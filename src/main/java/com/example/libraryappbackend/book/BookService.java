package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Transactional
    public void updateBook(Long bookID, Book book) {
        Book toUpdate = this.bookRepository.findById(bookID).orElseThrow(NoSuchElementException::new);

        toUpdate.setTitle(book.getTitle());
        toUpdate.setPublishedDate(book.getPublishedDate());
        toUpdate.setStatus(book.getStatus());
        toUpdate.setIsWith(book.getIsWith());
    }

    @Transactional
    public void addNewBook(Book book) throws AlreadyExistsException {
        List<Book> booksWithTitle = bookRepository.findByTitle(book.getTitle());

        if(booksWithTitle.contains(book)){
            throw new AlreadyExistsException(book);
        }

        this.bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(NoSuchElementException::new);
        book.getAuthors().forEach(author -> {
            author.unregisterBook(book);
        });
        this.bookRepository.delete(book);
    }


}
