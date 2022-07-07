package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;

@RestController
@RequestMapping(ROOT_API_ROUTE + "/author")
public class AuthorController {

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable String id){
        return this.authorService.getAuthorById(Long.parseLong(id));
    }

    @GetMapping
    public List<Author> getListOfAuthors(){
        return this.authorService.getListOfAuthors();
    }

    @GetMapping("/{id}/books")
    public List<Book> getBooksWrittenByAuthor(@PathVariable String id){
        return this.authorService.getBooksWrittenByAuthorWithId(Long.parseLong(id));
    }

    @PutMapping("/{id}")
    public void updateAuthorInformation(@PathVariable String id, @RequestBody Author author){
        this.authorService.updateAuthorInformation(Long.parseLong(id), author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable String id){
        this.authorService.deleteAuthor(Long.parseLong(id));
    }
}
