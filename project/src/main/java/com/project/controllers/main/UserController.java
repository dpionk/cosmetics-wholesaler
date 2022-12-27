package com.project.controllers.main;

import com.project.repositories.CartRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

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
