package kz.bitlab.cars_g136.service.impl;

import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Country;
import kz.bitlab.cars_g136.repository.CarRepository;
import kz.bitlab.cars_g136.repository.CategoryRepository;
import kz.bitlab.cars_g136.repository.CountryRepository;
import kz.bitlab.cars_g136.service.CarService;
import kz.bitlab.cars_g136.util.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Car> getCars(String orderBy, Pageable pageable,
                             Integer year, String name, Double price,
                             Long countryId, Long categoryId) {
        pageable = getOrderBy(orderBy, pageable);
        var carSpecification = CarSpecification.getCarSpec(year, name, price, countryId, categoryId);
        return carRepository.findAll(carSpecification, pageable);
    }

    @Override
    public void saveCar(String name, String model, int year, double price, Long countryId) {
        var country = countryRepository.findById(countryId).orElse(null);
        var car = buildCar(name, model, year, price, country);

        carRepository.save(car);
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCar(Long id, String name, String model, int year, double price, Long countryId) {
        var car = carRepository.findById(id).orElse(null);
        var country = countryRepository.findById(countryId).orElse(null);

        if (car == null) return;

        carSetter(car, name, model, year, price, country);
        carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public void assignCategory(Long carId, Long categoryId) {
        var car = carRepository.findById(carId).orElse(null);
        var category = categoryRepository.findById(categoryId).orElse(null);

        if (car == null || category == null) return;
        if (car.getCategories() == null) car.setCategories(new ArrayList<>());

        car.getCategories().add(category);
        carRepository.save(car);
    }

    @Override
    public void removeCategory(Long carId, Long categoryId) {
        var car = carRepository.findById(carId).orElse(null);
        var category = categoryRepository.findById(categoryId).orElse(null);

        if (car == null || category == null) return;

        car.getCategories().remove(category);
        carRepository.save(car);
    }

    private Pageable getOrderBy(String orderBy, Pageable pageable) {
        if (orderBy != null) {
            if (orderBy.equalsIgnoreCase("asc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(pageable.getSort().iterator().next().getProperty()).ascending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(pageable.getSort().iterator().next().getProperty()).descending());
            }
        }
        return pageable;
    }

    private Car buildCar(String name, String model, int year, double price, Country country) {
        return Car.builder()
                .name(name)
                .model(model)
                .year(year)
                .price(price)
                .country(country)
                .build();
    }

    private void carSetter(Car car, String name, String model, int year, double price, Country country) {
        car.setName(name);
        car.setModel(model);
        car.setYear(year);
        car.setPrice(price);
        car.setCountry(country);
    }
}
