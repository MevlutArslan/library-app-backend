package com.example.libraryappbackend.author;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;

@RestController
@RequestMapping(ROOT_API_ROUTE + "/authors")
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
    public Set<Book> getBooksWrittenByAuthor(@PathVariable String id){
        return this.authorService.getBooksWrittenByAuthorWithId(Long.parseLong(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED) // HTTP 201
    public void createAuthor(@RequestBody Author author) throws AlreadyExistsException {
        this.authorService.createAuthor(author);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT) // HTTP 204
    public void updateAuthorInformation(@PathVariable String id, @RequestBody Author author){
        this.authorService.updateAuthorInformation(Long.parseLong(id), author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable String id){
        this.authorService.deleteAuthor(Long.parseLong(id));
    }

    @PostMapping("/{id}/registerBookToAuthor/{bookID}")
    public void registerBookToAuthor(@PathVariable String id, @PathVariable String bookID){
        this.authorService.registerBookToAuthor(
                Long.parseLong(id),
                Long.parseLong(bookID)
        );
    }

    @PostMapping("/{id}/removeBookFromAuthor/{bookID}")
    public void removeBookFromAuthor(@PathVariable String id, @PathVariable String bookID){
        this.authorService.removeBookFromAuthor(
                Long.parseLong(id),
                Long.parseLong(bookID)
        );
    }

}
