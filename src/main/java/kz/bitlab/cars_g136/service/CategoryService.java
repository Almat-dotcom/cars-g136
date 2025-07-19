package kz.bitlab.cars_g136.service;

import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryService {
    List<Category> getAllByCarsParam(Long  id);

    List<Category> getAll();

    void addCategory(String name);
}
