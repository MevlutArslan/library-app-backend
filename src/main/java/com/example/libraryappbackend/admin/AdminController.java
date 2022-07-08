package com.example.libraryappbackend.admin;

import com.example.libraryappbackend.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.libraryappbackend.utility.Constants.ROOT_API_ROUTE;

@RestController
@RequestMapping(ROOT_API_ROUTE + "/admins")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getListOfAdmins(){
        return this.adminService.getListOfAdmins();
    }

    @PostMapping("/login")
    public void login(){
//        this.adminService.login(username, password);
    }

    @PostMapping("/logout")
    public void logout(){

    }

//    @PostMapping()
}
