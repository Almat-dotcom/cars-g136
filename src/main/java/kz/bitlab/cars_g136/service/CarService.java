package kz.bitlab.cars_g136.service;

import kz.bitlab.cars_g136.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {

    Page<Car> getCars(String orderBy, Pageable pageable,
                      Integer year, String name, Double price,
                      Long countryId, Long categoryId);

    void saveCar(String name, String model, int year, double price, Long countryId);

    Car getCarById(Long id);

    void updateCar(Long id, String name, String model, int year, double price, Long countryId);

    void deleteCar(Long id);

    void assignCategory(Long carId, Long categoryId);

    void removeCategory(Long carId, Long categoryId);
}
