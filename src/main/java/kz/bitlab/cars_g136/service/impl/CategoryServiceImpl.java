package kz.bitlab.cars_g136.service.impl;

import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Category;
import kz.bitlab.cars_g136.repository.CarRepository;
import kz.bitlab.cars_g136.repository.CategoryRepository;
import kz.bitlab.cars_g136.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CarRepository carRepository;

    @Override
    public List<Category> getAllByCarsParam(Long id) {
        Car car = carRepository.findById(id).orElse(null);
        return categoryRepository.findAllByCarsNotContaining(car);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(String name) {
        Category category = Category.builder()
                .name(name)
                .build();
        categoryRepository.save(category);
    }
}
