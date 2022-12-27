package com.project.controllers.main;

import com.project.domains.Cart;
import com.project.domains.Cosmetic;
import com.project.repositories.CartRepository;
import com.project.repositories.CosmeticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CartController {

    @Autowired
    CosmeticRepository cosmeticRepository;

    @Autowired
    CartRepository cartRepository;

    @GetMapping("/cart/{id}")
    public String cartPage(Model model) {
        return "/cart/cart";
    }

    @PostMapping("/cart/{id}")
    public String addCosmeticToCart(@Valid Cosmetic cosmetic, @PathVariable Long id, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Not valid data passed when trying to add cosmetic to cart! Try again.");
        }

        cartRepository.findById(id).get().addCosmeticToCart(cosmetic);
        redirectAttributes.addFlashAttribute("success", "Succesfully added cosmetic to cart");

        return "redirect:/cart/" + id.toString();
    }

    @GetMapping("/cart/{id}/delete")
    public void  deleteCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            var cosmeticsInCart = cosmeticRepository.findCosmeticsByCart(cartRepository.findById(id).get());
            for ( Cosmetic cosmetic : cosmeticsInCart
                 ) {
                cosmetic.deleteCartfromSet(cartRepository.findById(id).get());
            }
            cartRepository.delete(cart.get());
            redirectAttributes.addFlashAttribute("success", String.format(
                    "Cart with ID %d has been successfully deleted.",
                    cart.get().getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Couldn't delete cart with ID %d",
                    cart.get().getId()));
        }
    }
    @GetMapping("/cart/{id}/deleteCosmeticFromCart/{cosmeticId}")
    public void  deleteFromCart(@PathVariable Long id, @PathVariable Long cosmeticId, RedirectAttributes redirectAttributes) {
        var cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            Cosmetic cosmeticInCart = cosmeticRepository.findById(cosmeticId).get();
            cosmeticInCart.deleteCartfromSet(cartRepository.findById(id).get());

            cart.get().deleteCosmeticFromCart(cosmeticInCart);

            redirectAttributes.addFlashAttribute("success", String.format(
                    "Cosmetic with ID %d has been successfully deleted from your cart",
                    cart.get().getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Couldn't find cart with ID %d",
                    cart.get().getId()));
        }
    }

}
