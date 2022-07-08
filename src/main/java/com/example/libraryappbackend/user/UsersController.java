package com.example.libraryappbackend.user;

import com.example.libraryappbackend.book.Book;
import com.example.libraryappbackend.exceptions.AlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;

@RestController
@RequestMapping(ROOT_API_ROUTE + "/users")
public class UsersController {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService){
        this.usersService = usersService;
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable String id){
        return this.usersService.getUserById(Long.parseLong(id));
    }

    @GetMapping
    public List<Users> getListOfAllUsers(){
        return this.usersService.getListOfAllUsers();
    }

    @PostMapping
    public void addNewUser(@RequestBody Users user) throws AlreadyExistsException {
        this.usersService.saveUser(user);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody Users user){
        this.usersService.updateUser(Long.parseLong(id), user);
    }

    @PostMapping("/{id}/registerBook/{bookID}")
    public void registerBookToUser(@PathVariable String id, @PathVariable String bookID){
        this.usersService.registerBookToUser(id, bookID);
    }

    @PostMapping("/{id}/returnBook/{bookID}")
    public void returnBookFromUser(@PathVariable String id, @PathVariable String bookID){
        this.usersService.returnBookFromUser(id, bookID);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        this.usersService.deleteUser(Long.parseLong(id));
    }

    @GetMapping("/{id}/under_possesion")
    public List<Book> getListOfAllBooksUnderPossession(@PathVariable String id){
        return this.usersService.getListOfAllBooksUnderPossession(Long.parseLong(id));
    }

}
