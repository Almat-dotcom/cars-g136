package kz.bitlab.cars_g136.controller;

import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Category;
import kz.bitlab.cars_g136.entity.Country;
import kz.bitlab.cars_g136.service.CarService;
import kz.bitlab.cars_g136.service.CategoryService;
import kz.bitlab.cars_g136.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final CountryService countryService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String start(Model model,
                        @RequestParam(name = "year", required = false) Integer year,
                        @RequestParam(name = "name", required = false) String name,
                        @RequestParam(name = "price", required = false) Double price,
                        @RequestParam(name = "country_id", required = false) Long countryId,
                        @RequestParam(name = "category_id", required = false) Long categoryId,
                        @RequestParam(name = "sort_order", required = false) String orderBy,
                        @PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC)
                        Pageable pageable) {
        Page<Car> cars = carService.getCars(orderBy, pageable, year, name, price, countryId, categoryId);
        model.addAttribute("chosenCountryId", countryId);
        model.addAttribute("chosenCategoryId", categoryId);
        model.addAttribute("cars", cars);
        model.addAttribute("countries", countryService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("currentPage", pageable.getPageNumber());
        List<Integer> pages = IntStream.range(0, cars.getTotalPages()).boxed().toList();
        // getTotalPages=4 IntStream(0,14).box.toList List<0,1,2,3,4,5,6,7,8,9,10,11,12,13>
        model.addAttribute("pageNumbers", pages);
        model.addAttribute("pageSize", cars.getTotalPages());


        model.addAttribute("sortOrderCustom", orderBy);
        model.addAttribute("sortSizeCustom", pageable.getPageSize());
        model.addAttribute("sortNameCustom", pageable.getSort().iterator().next().getProperty());
        return "start";
    }

    @GetMapping("/add-page")
    public String addPage(Model model) {
        List<Country> countries = countryService.getAll();
        model.addAttribute("countries", countries);
        return "add-car";
    }

    @PostMapping("/add-car")
    public String addCar(@RequestParam(name = "name") String name,
                         @RequestParam(name = "model") String model,
                         @RequestParam(name = "year") int year,
                         @RequestParam(name = "price") double price,
                         @RequestParam(name = "car_country") Long countryId) {
        carService.saveCar(name, model, year, price, countryId);
        return "redirect:/";
    }

    @GetMapping("car-view/{id}")
    public String carView(Model model,
                          @PathVariable Long id) {
        Car car = carService.getCarById(id);
        List<Category> categories = categoryService.getAllByCarsParam(id);
        List<Country> countries = countryService.getAll();
        model.addAttribute("car", car);
        model.addAttribute("countries", countries);
        model.addAttribute("categories", categories);
        return "car-view";
    }

    @PostMapping("/update-car")
    public String updateCar(@RequestParam(name = "id") Long id,
                            @RequestParam(name = "name") String name,
                            @RequestParam(name = "model") String model,
                            @RequestParam(name = "year") int year,
                            @RequestParam(name = "price") double price,
                            @RequestParam(name = "car_country") Long countryId) {
        carService.updateCar(id, name, model, year, price, countryId);
        return "redirect:/";
    }


    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam(name = "id") Long id) {
        carService.deleteCar(id);
        return "redirect:/";
    }

    @PostMapping("/assign-category")
    public String assignCategory(@RequestParam(name = "carId") Long carId,
                                 @RequestParam(name = "categoryId") Long categoryId) {
        carService.assignCategory(carId, categoryId);
        return "redirect:/car-view/" + carId;
    }

    @PostMapping("/remove-category")
    public String unassignCategory(@RequestParam(name = "carId") Long carId,
                                   @RequestParam(name = "categoryId") Long categoryId) {
        carService.removeCategory(carId, categoryId);
        return "redirect:/car-view/" + carId;
    }
}
