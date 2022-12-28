package com.project.controllers.supporting;

import com.project.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {
    private final UserRepository userRepository;

    public HomePageController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        var currentUserUserName = auth.getName();
        var currentUserIsAdmin = userRepository.getAllUsersWithUsername(currentUserUserName).get(0).getIs_admin();
        model.addAttribute("username", currentUserUserName);
        model.addAttribute("isAdmin", currentUserIsAdmin);
        return "home";
    }
}
