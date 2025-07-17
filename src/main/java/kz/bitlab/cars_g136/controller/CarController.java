package kz.bitlab.cars_g136.controller;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Category;
import kz.bitlab.cars_g136.entity.Country;
import kz.bitlab.cars_g136.repository.CarRepository;
import kz.bitlab.cars_g136.repository.CategoryRepository;
import kz.bitlab.cars_g136.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class CarController {
    private final CarRepository carRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;

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
        Page<Car> cars = null;
        Pageable pageable1 = null;
        if (orderBy != null) {
            if (orderBy.equalsIgnoreCase("asc")) {
                pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(pageable.getSort().iterator().next().getProperty()).ascending());
            } else {
                pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(pageable.getSort().iterator().next().getProperty()).descending());
            }
        }
        //
        Specification<Car> carSpecification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("year"), year));
            }
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (price != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), price));
            }
            if (countryId != null) {
                Join<Car, Country> countryJoin = root.join("country");
                predicates.add(criteriaBuilder.equal(countryJoin.get("id"), countryId));
            }
            if (categoryId != null) {
                Join<Car, Category> categoryJoin = root.join("categories");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }
            Predicate commonPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            return commonPredicate;
        };
        if (pageable1 != null) {
            cars = carRepository.findAll(carSpecification, pageable1);
        } else {
            cars = carRepository.findAll(carSpecification, pageable);
        }
        model.addAttribute("chosenCountryId", countryId);
        model.addAttribute("chosenCategoryId", categoryId);
        model.addAttribute("cars", cars);
        model.addAttribute("countries", countryRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
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
        List<Country> countries = countryRepository.findAll();
        model.addAttribute("countries", countries);
        return "add-car";
    }

    @PostMapping("/add-car")
    public String addCar(@RequestParam(name = "name") String name,
                         @RequestParam(name = "model") String model,
                         @RequestParam(name = "year") int year,
                         @RequestParam(name = "price") double price,
                         @RequestParam(name = "car_country") Long countryId) {
        Country country = countryRepository.findById(countryId).orElse(null);
        Car car = Car.builder()
                .name(name)
                .model(model)
                .year(year)
                .price(price)
                .build();
        carRepository.save(car);
        return "redirect:/";
    }

    @GetMapping("car-view/{id}")
    public String carView(Model model,
                          @PathVariable Long id) {
        Car car = carRepository.findById(id).orElse(null);
        List<Category> categories = categoryRepository.findAllByCarsNotContaining(car);
        List<Country> countries = countryRepository.findAll();
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
        Car car = carRepository.findById(id).orElse(null);
        Country country = countryRepository.findById(countryId).orElse(null);
        if (car == null) {
            return "redirect:/";
        }
        car.setName(name);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
//        car.setCountry(country);
        carRepository.save(car);
        return "redirect:/";
    }


    @PostMapping("/delete-car")
    public String deleteCar(@RequestParam(name = "id") Long id) {
        carRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/assign-category")
    public String assignCategory(@RequestParam(name = "carId") Long carId,
                                 @RequestParam(name = "categoryId") Long categoryId) {
        Car car = carRepository.findById(carId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (car == null || category == null) {
            return "reirect:/";
        }
        car.getCategories().add(category);
        carRepository.save(car);
        return "redirect:/car-view/" + carId;
    }

    @PostMapping("/remove-category")
    public String unassignCategory(@RequestParam(name = "carId") Long carId,
                                   @RequestParam(name = "categoryId") Long categoryId) {
        Car car = carRepository.findById(carId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (car == null || category == null) {
            return "reirect:/";
        }
        car.getCategories().remove(category);
        carRepository.save(car);
        return "redirect:/car-view/" + carId;
    }
}
