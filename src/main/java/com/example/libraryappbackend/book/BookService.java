package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.BookNotAvailableException;
import com.example.libraryappbackend.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    public List<Book> getListOfBooks(){
        return this.bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id){
        return this.bookRepository.findById(id);
    }

    public List<Book> getListOfBooksByAuthor(String author){
        return this.bookRepository.findByAuthor(author);
    }

    public List<Book> getListOfBooksByTitle(String title){
        return this.bookRepository.findByTitle(title);
    }

    public void updateTitleOfBook(Long id, String title){
        Optional<Book> toUpdate = this.bookRepository.findById(id);
        toUpdate.ifPresent(book -> book.setTitle(title));
    }

    public void rentBook(Long id, Users user){
        Optional<Book> toRent = this.bookRepository.findById(id);

        toRent.ifPresent((book)->{
            if(book.getStatus().equals(BookStatus.AVAILABLE)){
                user.getBooksUnderPossession().add(book);
                book.setCurrentlyWith(user);
                book.setStatus(BookStatus.OCCUPIED);
            }else{
                // TODO :Don't really remember how to use custom exceptions... Will need to look here again
                try {
                    throw new BookNotAvailableException(book);
                } catch (BookNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void returnBook(Long id){
        Optional<Book> toReturn = this.bookRepository.findById(id);

        toReturn.ifPresent((book)-> {
            book.getCurrentlyWith().getBooksUnderPossession().remove(book);
            book.setCurrentlyWith(null);
            book.setStatus(BookStatus.AVAILABLE);
        });
    }

    public void addNewBook(Book book){
        this.bookRepository.save(book);
    }

    public void deleteBook(Long id){
        Optional<Book> toDelete = this.bookRepository.findById(id);
        toDelete.ifPresent((book)-> {
            this.bookRepository.delete(book);
        });
    }

}
