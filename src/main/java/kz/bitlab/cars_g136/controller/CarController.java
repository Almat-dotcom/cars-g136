package kz.bitlab.cars_g136.controller;

import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Country;
import kz.bitlab.cars_g136.repository.CarRepository;
import kz.bitlab.cars_g136.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CarController {
    private final CarRepository carRepository;
    private final CountryRepository countryRepository;
    //CRUD

    @GetMapping("/")
    public String start(Model model) {
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
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
        List<Country> countries = countryRepository.findAll();
        model.addAttribute("car", car);
        model.addAttribute("countries", countries);
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
}
