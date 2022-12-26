package com.project.controllers.supporting;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministrationController {

    @GetMapping("/administration")
    public String administrationPanel() {
        return "administration";
    }
}
