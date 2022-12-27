package com.project.controllers.main;

import com.project.domains.Cart;
import com.project.domains.User;
import com.project.repositories.CartRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/users")
    public String usersListPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/users/usersList";
    }


    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        var user = new User();
        model.addAttribute("user", user);
        return "users/addUser";
    }
    @PostMapping("/users")
    public String addUser(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Not valid data passed when trying to add new user! Try again.");
            return "users/addUser";
        }

        if (isUsernameAlreadyReserved(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", String.format("Cannot add user with username %s as this username is already reserved!", user.getUsername()));
            return "users/addUser";
        }

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Successfully added new user");

        return "redirect:/users/";
    }


    @GetMapping("/users/{id}/edit")
    public String showUpdateUserForm(@PathVariable long id, Model model) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));

        model.addAttribute("user", user);
        return "users/editUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable long id, @Valid User user, RedirectAttributes redirectAttrs) {
        var userToUpdate = userRepository.findById(id);

        if (userToUpdate.isPresent()) {
            userToUpdate.get().setUsername(user.getUsername());
            userToUpdate.get().setPassword(user.getPassword());
            userToUpdate.get().setIs_admin(user.getIs_admin());

            userRepository.save(userToUpdate.get());
            redirectAttrs.addFlashAttribute("success", String.format("Successfully updated data for user with ID %d!", id));
        } else {
            redirectAttrs.addFlashAttribute("error", String.format("Couldn't find user with ID %s !", id));
        }
        return "redirect:/users/";
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable long id, RedirectAttributes redirectAttrs) {
        var user = userRepository.findById(id);

        if (user.isPresent()) {
//            Cart userCart = cartRepository.findCartByUser(user.get()).get(0);
//            cartRepository.delete(userCart);
            userRepository.delete(user.get());

            redirectAttrs.addFlashAttribute("success", String.format("Successfully deleted user with ID %d!", id));
        } else {
            redirectAttrs.addFlashAttribute("error", String.format("Couldn't find user with ID %d, so it cannot be deleted!", id));
        }
        return "redirect:/users";
    }
    private boolean isUsernameAlreadyReserved(String username) {
        return !userRepository.getAllUsersWithUsername(username).isEmpty();
    }

}
