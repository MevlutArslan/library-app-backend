package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    public List<Author> getListOfAuthors(){
        return this.authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return this.authorRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Book> getBooksWrittenByAuthorWithId(Long id){
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
}
