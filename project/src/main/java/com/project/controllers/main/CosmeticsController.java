package com.project.controllers.main;

import com.project.domains.Cart;
import com.project.domains.Category;
import com.project.domains.Cosmetic;
import com.project.domains.User;
import com.project.exception.ResourceNotFoundException;
import com.project.repositories.CartRepository;
import com.project.repositories.CategoryRepository;
import com.project.repositories.CosmeticRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CosmeticsController {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CosmeticRepository cosmeticRepository;

    @GetMapping("/cosmetics")
    public String cosmeticsListPage(Model model) {
        model.addAttribute("cosmetics", cosmeticRepository.findAll());
        return "/cosmetics/cosmeticsList";
    }

    @PostMapping("/cosmetics")
    public String addCosmetic(@Valid Cosmetic cosmetic, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Not valid data passed when trying to add new cosmetic! Try again.");
            return "cosmetics/addCosmetic";
        }

        cosmeticRepository.save(cosmetic);
        redirectAttributes.addFlashAttribute("success", "Succesfully added new cosmetic");

        return "redirect:/cosmetics";
    }


    @GetMapping("/cosmetics/{id}")
    public String cosmeticDetailsPage(@PathVariable Long id, Model model) {

        var auth =  SecurityContextHolder.getContext().getAuthentication();
        var currentUserUserName = auth.getName();
        User currentUser = userRepository.getAllUsersWithUsername(currentUserUserName).get(0);

        var cosmetic = cosmeticRepository.findById(id);

        var cart = cartRepository.findCartByUser(currentUser);

        if (cart.size() != 0) {
            model.addAttribute("cart", cart.get(0));
            model.addAttribute("doesCartExist", true);
        } else {
            Cart newCart = new Cart();
            model.addAttribute("cart", newCart);
            model.addAttribute("doesCartExist", false);
        }

        if (cosmetic.isPresent()) {
            model.addAttribute("cosmetic", cosmetic.get());
            model.addAttribute("user", currentUser);
        } else {
            throw new ResourceNotFoundException("cosmetic", "cosmeticId", id);
        }
        return "/cosmetics/cosmetic";
    }

    @GetMapping("/cosmetics/{id}/edit")
    public String cosmeticEditDetailsPage(@PathVariable Long id, Model model) {
        var cosmetic = cosmeticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("cosmetic", "cosmeticId", id));
        model.addAttribute("cosmetic", cosmetic);
        model.addAttribute("categories", categoryRepository.findAll());
        return "/cosmetics/editCosmetic";
    }

    @PostMapping("/cosmetics/{id}")
    public String updateCosmetic(@PathVariable Long id, @Valid Cosmetic cosmetic, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        var cosmeticById = cosmeticRepository.findById(id);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid data passed for updating cosmetic object.");
        } else if (cosmeticById.isPresent()) {

            var cosmeticToUpdate = cosmeticById.get();
            copyCosmeticData(cosmeticToUpdate, cosmetic);
            cosmeticRepository.save(cosmeticToUpdate);

            redirectAttributes.addFlashAttribute("success",
                    String.format("Successfully updated data for cosmetic with ID %d!", id));
        } else {
            redirectAttributes.addFlashAttribute("error",
                    String.format("Couldn't retrieve cosmetic with ID %s", id));
        }
        return "redirect:/cosmetics/" + id;
    }

    @GetMapping("/cosmetics/add")
    public String cosmeticAddPage(Model model) {
        Cosmetic cosmetic = new Cosmetic();
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cosmetic", cosmetic);
        return "/cosmetics/addCosmetic";
    }

    @GetMapping("/cosmetics/{id}/delete")
        public String  deleteCosmetic(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            var cosmetic = cosmeticRepository.findById(id);
            if (cosmetic.isPresent()) {
                cosmeticRepository.delete(cosmetic.get());
                redirectAttributes.addFlashAttribute("success", String.format(
                        "Cosmetic with ID %d has been successfully deleted.",
                        cosmetic.get().getId()));
            } else {
                redirectAttributes.addFlashAttribute("error", String.format("Couldn't delete cosmetic with ID %d",
                        cosmetic.get().getId()));
            }
            return "redirect:/cosmetics";
    }

    private void copyCosmeticData(Cosmetic cosmeticFrom, Cosmetic cosmeticTo) {
        cosmeticFrom.setName(cosmeticTo.getName());
        cosmeticFrom.setPrice(cosmeticTo.getPrice());
        cosmeticFrom.setCategory(cosmeticTo.getCategory());
        cosmeticFrom.setImage_url(cosmeticTo.getImage_url());
    }
}
