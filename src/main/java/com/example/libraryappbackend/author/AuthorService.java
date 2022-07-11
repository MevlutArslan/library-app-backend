package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.book.BookRepository;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import com.sun.jdi.VMCannotBeModifiedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository){
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void createAuthor(Author author) throws AlreadyExistsException {
        Author authorByName = this.authorRepository.findAuthorByName(author.getName());

        if(authorByName != null){
            throw new AlreadyExistsException(author);
        }

        this.authorRepository.save(author);
    }

    public List<Author> getListOfAuthors(){
        return this.authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return this.authorRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Set<Book> getBooksWrittenByAuthorWithId(Long id){
        return this.authorRepository.findById(id).orElseThrow(NoSuchElementException::new).getBooksWritten();
    }

    @Transactional
    public void updateAuthorInformation(Long id, Author updatedAuthor){
        Author author = this.authorRepository.findById(id).orElseThrow(NoSuchElementException::new);

        author.setName(updatedAuthor.getName());
        author.setSurname(updatedAuthor.getSurname());
    }

    public void deleteAuthor(Long id){
        Author author = this.authorRepository.findById(id).orElseThrow(NoSuchElementException::new);
        this.authorRepository.delete(author);
    }

    @Transactional
    public void registerBookToAuthor(Long authorID, Long bookID){
        Author author = this.authorRepository.findById(authorID).orElseThrow(NoSuchElementException::new);
        Book book = this.bookRepository.findById(bookID).orElseThrow(NoSuchElementException::new);

        author.registerBook(book);
    }

    @Transactional
    public void removeBookFromAuthor(Long authorID, Long bookID){
        Author author = this.authorRepository.findById(authorID).orElseThrow(NoSuchElementException::new);
        Book book = this.bookRepository.findById(bookID).orElseThrow(NoSuchElementException::new);

        author.unregisterBook(book);
    }
}
