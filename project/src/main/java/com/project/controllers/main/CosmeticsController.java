package com.project.controllers.main;

import com.project.domains.Category;
import com.project.domains.Cosmetic;
import com.project.repositories.CategoryRepository;
import com.project.repositories.CosmeticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CosmeticsController {

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
    public String addCosmetic(Cosmetic cosmetic, RedirectAttributes redirectAttributes) {
        cosmeticRepository.save(cosmetic);
        redirectAttributes.addFlashAttribute("success", "Succesfully added new cosmetic");

        return "redirect:/cosmetics";
    }


    @GetMapping("/cosmetics/{id}")
    public String cosmeticDetailsPage(@PathVariable Long id, Model model) {
        Cosmetic cosmetic = cosmeticRepository.findById(id).get();
        model.addAttribute("cosmetic", cosmetic);
        return "/cosmetics/cosmetic";
    }

    @GetMapping("/cosmetics/{id}/edit")
    public String cosmeticEditDetailsPage(@PathVariable Long id, Model model) {
        Cosmetic cosmetic = cosmeticRepository.findById(id).get();
        model.addAttribute("cosmetic", cosmetic);
        return "/cosmetics/editCosmetic";
    }

    @GetMapping("/cosmetics/add")
    public String cosmeticAddPage(Model model) {
        Cosmetic cosmetic = new Cosmetic();
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cosmetic", cosmetic);
        return "/cosmetics/addCosmetic";
    }

    @DeleteMapping("/cosmetics/{id}")
        public String  deleteCosmetic(@PathVariable Long id) {
            var cosmetic = cosmeticRepository.findById(id);
            if (cosmetic.isPresent()) {
                cosmeticRepository.delete(cosmetic.get());

                return "redirect:/cosmetics";
            }
            return "/";
    }
}
