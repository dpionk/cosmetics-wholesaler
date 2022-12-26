package com.project.controllers.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/users")
    public String usersListPage() {
        return "/users/usersList";
    }

    @GetMapping("/users/{id}")
    public String userDetailsPage() {
        return "/users/user";
    }

    @GetMapping("/users/{id}/edit")
    public String userEditDetailsPage() {
        return "/users/editUser";
    }

}
