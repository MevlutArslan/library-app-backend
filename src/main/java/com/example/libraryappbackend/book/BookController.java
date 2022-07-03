package com.example.libraryappbackend.book;

import com.example.libraryappbackend.exceptions.BookIsAlreadyInDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping()
    public List<Book> getListOfAllBooks(){
        return this.bookService.getListOfBooks();
    }

    @PostMapping()
    public void addBook(@RequestBody Book book){
        this.bookService.addNewBook(book);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable String id,@RequestBody Book book){
        this.bookService.updateBook(Long.parseLong(id), book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id){
        this.bookService.deleteBook(Long.parseLong(id));
    }


}
