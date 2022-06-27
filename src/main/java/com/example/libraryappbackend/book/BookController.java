package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.BookIsAlreadyInDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;

@RestController
@RequestMapping(ROOT_API_ROUTE + "/book")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable String id){
        return this.bookService.getBookById(Long.parseLong(id));
    }

    @PostMapping()
    public void addBook(@RequestBody Book book){
        this.bookService.addNewBook(book);
    }

    @GetMapping("/books")
    public List<Book> getListOfAllBooks(){
        return this.bookService.getListOfBooks();
    }

    // Exceptions return an "Internal Server Error" with the exception message
    @PostMapping("/return/{id}")
    public void returnBook(@PathVariable String id) throws BookIsAlreadyInDatabaseException {
        this.bookService.returnBook(Long.parseLong(id));
    }

    @DeleteMapping("{id}")
    public void deleteBook(@PathVariable String id){
        this.bookService.deleteBook(Long.parseLong(id));
    }


}
