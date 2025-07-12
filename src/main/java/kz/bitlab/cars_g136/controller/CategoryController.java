package kz.bitlab.cars_g136.controller;

import kz.bitlab.cars_g136.entity.Category;
import kz.bitlab.cars_g136.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping("/add-category")
    public String addCategoryPage() {
        return "add-category";
    }

    @PostMapping("/add-category")
    public String addCategory(@RequestParam(name = "name") String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        categoryRepository.save(category);
        return "redirect:";
    }
}
