package com.project.controllers.main;

import com.project.domains.Category;
import com.project.domains.Cosmetic;
import com.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public String categoriesListPage(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "/categories/categoriesList";
    }

    @PostMapping("/categories")
    public String addCosmetic(Category category, RedirectAttributes redirectAttributes) {
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Succesfully added new category");

        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String categoryEditDetailsPage(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).get();
        model.addAttribute("category", category);
        return "/categories/{id}/edit";
    }

    @GetMapping("/categories/add")
    public String categoryAddPage(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "/categories/addCategory";
    }

}
