package com.project.controllers.main;

import com.project.domains.Cart;
import com.project.domains.Cosmetic;
import com.project.domains.User;
import com.project.repositories.CartRepository;
import com.project.repositories.CosmeticRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CosmeticRepository cosmeticRepository;

    @Autowired
    CartRepository cartRepository;

    @GetMapping("/cart")
    public String cartPage(Model model) {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        var currentUserUserName = auth.getName();
        User currentUser = userRepository.getAllUsersWithUsername(currentUserUserName).get(0);

        var cart = cartRepository.findCartByUser(currentUser);

        if (cart.size() != 0) {
            model.addAttribute("cart", cart.get(0));

            var productsInCart = cosmeticRepository.findCosmeticsByCart(cart.get(0));

            Map<Cosmetic, Integer> productsInCartWithQuantity = new HashMap<>();
            double total = 0;

            for ( Cosmetic cosmetic : productsInCart
                 ) {
                if (productsInCartWithQuantity.containsKey(cosmetic)) {
                    productsInCartWithQuantity.put(cosmetic, productsInCartWithQuantity.get(cosmetic) + 1);
                }
                else {
                    productsInCartWithQuantity.put(cosmetic, 1);
                }

                total += cosmetic.getPrice();
            }


            model.addAttribute("productsInCart", productsInCartWithQuantity);
            model.addAttribute("doesCartExist", true);
            model.addAttribute("total", total);
        } else {
            Cart newCart = new Cart();
            model.addAttribute("cart", newCart);
            model.addAttribute("doesCartExist", false);
        }

        model.addAttribute("currentUser", currentUser);
        return "cart/cart";
    }

    @PostMapping("/createCart/{id}")
    public String createCartForUser(@Valid Cart cart, @PathVariable Long id, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Not valid data passed when trying to add  cart! Try again.");
        }
        cartRepository.save(cart);
        redirectAttributes.addFlashAttribute("success", "Succesfully created cart");

        return "redirect:/cart";
    }

    @PostMapping("/cart/add/{cosmeticId}")
    public String addCosmeticToCart( @PathVariable long cosmeticId, RedirectAttributes redirectAttributes) {
        var auth =  SecurityContextHolder.getContext().getAuthentication();
        var currentUserUserName = auth.getName();
        User currentUser = userRepository.getAllUsersWithUsername(currentUserUserName).get(0);

        var cart = cartRepository.findCartByUser(currentUser);
        var cosmeticById = cosmeticRepository.findById(cosmeticId);

        var isCosmeticPresent = cosmeticById.isPresent();
        var isCartPresent = cart.size() > 0;

        if (isCosmeticPresent && isCartPresent) {

            Cart cartPresent = cart.get(0);
            Cosmetic cosmeticPresent = cosmeticById.get();

            cartPresent.addCosmeticToCart(cosmeticById.get());
            cosmeticPresent.addCartToSet(cart.get(0));
            cartRepository.save(cartPresent);
            cosmeticRepository.save(cosmeticPresent);
            redirectAttributes.addFlashAttribute("success", "Succesfully added cosmetic to cart");
            return "redirect:/cart";

        }

        redirectAttributes.addFlashAttribute("error", "Could not add cosmetic to cart");
        return "redirect:/cosmetics";


    }

    @GetMapping("/cart/{id}/delete")
    public String  deleteCart(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            var cosmeticsInCart = cosmeticRepository.findCosmeticsByCart(cartRepository.findById(id).get());
            for ( Cosmetic cosmetic : cosmeticsInCart
                 ) {
                cosmetic.deleteCartfromSet(cartRepository.findById(id).get());
                cosmeticRepository.save(cosmetic);
            }
            cartRepository.delete(cart.get());
            redirectAttributes.addFlashAttribute("success", String.format(
                    "Cart with ID %d has been successfully deleted.",
                    cart.get().getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Couldn't delete cart with ID %d",
                    cart.get().getId()));
        }
        return "redirect:/cart";
    }
    @PostMapping("/cart/deleteCosmeticFromCart/{cosmeticId}")
    public String  deleteFromCart( @PathVariable Long cosmeticId, RedirectAttributes redirectAttributes) {

        var auth =  SecurityContextHolder.getContext().getAuthentication();
        var currentUserUserName = auth.getName();
        User currentUser = userRepository.getAllUsersWithUsername(currentUserUserName).get(0);

        var cart = cartRepository.findCartByUser(currentUser);

        if (cart.size() > 0) {
            Cosmetic cosmeticInCart = cosmeticRepository.findById(cosmeticId).get();
            cosmeticInCart.deleteCartfromSet(cart.get(0));

            cart.get(0).deleteCosmeticFromCart(cosmeticInCart);

            cartRepository.save(cart.get(0));
            cosmeticRepository.save(cosmeticInCart);

            redirectAttributes.addFlashAttribute("success", String.format(
                    "Cosmetic with ID %d has been successfully deleted from your cart",
                    cart.get(0).getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Couldn't find cart with ID %d",
                    cart.get(0).getId()));
        }
        return "redirect:/cart";
    }

}
