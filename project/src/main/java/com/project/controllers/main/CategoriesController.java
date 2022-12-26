package com.project.controllers.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoriesController {

    @GetMapping("/categories")
    public String categoriesListPage() {
        return "/categories/categoriesList";
    }

    @GetMapping("/categories/{id}/edit")
    public String categoryEditDetailsPage() {
        return "/categories/editCategory";
    }

    @GetMapping("/categories/add")
    public String categoryAddPage() {
        return "/categories/addCategory";
    }

}
