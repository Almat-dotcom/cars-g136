package kz.bitlab.cars_g136.controller;

import kz.bitlab.cars_g136.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/add-category")
    public String addCategoryPage() {
        return "add-category";
    }

    @PostMapping("/add-category")
    public String addCategory(@RequestParam(name = "name") String name) {
        categoryService.addCategory(name);
        return "redirect:";
    }
}
