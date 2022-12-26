package com.project.controllers.main;

import com.project.repositories.CosmeticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CosmeticsController {

    @Autowired
    private CosmeticRepository cosmeticRepository;

    @GetMapping("/cosmetics")
    public String cosmeticsListPage() {
            return "/cosmetics/cosmeticsList";
    }


    @GetMapping("/cosmetics/{id}")
    public String cosmeticDetailsPage() {
            return "/cosmetics/cosmetic";
    }

    @GetMapping("/cosmetics/{id}/edit")
    public String cosmeticEditDetailsPage() {
        return "/cosmetics/editCosmetic";
    }

    @GetMapping("/cosmetics/add")
    public String cosmeticAddPage() {
        return "/cosmetics/addCosmetic";
    }

}
