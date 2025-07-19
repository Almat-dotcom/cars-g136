package kz.bitlab.cars_g136.controller;

import kz.bitlab.cars_g136.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("add-country")
    public String addCountry() {
        return "add-country";
    }

    @PostMapping("/add-country")
    public String addCountry(@RequestParam(name = "name") String name,
                             @RequestParam(name = "code") String code) {
        countryService.addCountry(name, code);
        return "redirect:/";
    }
}
