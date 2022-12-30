package com.project.controllers.main;

import com.project.domains.Category;
import com.project.domains.Cosmetic;
import com.project.exception.ResourceNotFoundException;
import com.project.repositories.CategoryRepository;
import com.project.repositories.CosmeticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CosmeticRepository cosmeticRepository;

    @GetMapping("/categories")
    public String categoriesListPage(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "/categories/categoriesList";
    }

    @PostMapping("/categories")
    public String addCosmetic(@Valid Category category, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Not valid data passed when trying to add new category! Try again.");
            return "/categories/addCategory";
        }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("success", "Succesfully added new category");

        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String categoryEditDetailsPage(@PathVariable Long id, Model model) {
        var category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category", "categoryId", id));

        model.addAttribute("category", category);
        return "/categories/editCategory";
    }

    @GetMapping("/categories/add")
    public String categoryAddPage(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "/categories/addCategory";
    }

    @PostMapping("/categories/{id}")
    public String updateCosmetic(@PathVariable Long id, @Valid Category category, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        var categoryById = categoryRepository.findById(id);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid data passed for updating category object.");
        } else if (categoryById.isPresent()) {

            var cosmeticToUpdate = categoryById.get();
            copyCategoryData(cosmeticToUpdate, category);
            categoryRepository.save(cosmeticToUpdate);

            redirectAttributes.addFlashAttribute("success",
                    String.format("Successfully updated data for category with ID %d!", id));
        } else {
            redirectAttributes.addFlashAttribute("error",
                    String.format("Couldn't retrieve category with ID %s", id));
        }
        return "redirect:/categories/";
    }

    @GetMapping("/categories/{id}/delete")
    public String  deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var category = categoryRepository.findById(id);
        if (category.isPresent()) {
            var cosmeticsWithCategory = cosmeticRepository.findCosmeticsByCategory(categoryRepository.findById(id).get());
            for ( Cosmetic cosmetic : cosmeticsWithCategory
                 ) {
                cosmetic.setCategory(null);
            }
            categoryRepository.delete(category.get());
            redirectAttributes.addFlashAttribute("success", String.format(
                    "Category with ID %d has been successfully deleted.",
                    category.get().getId()));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Couldn't delete category with ID %d",
                    category.get().getId()));
        }
        return "redirect:/categories";
    }

    private void copyCategoryData(Category categoryFrom, Category categoryTo) {
        categoryFrom.setName(categoryTo.getName());
    }

}
