package kz.bitlab.cars_g136.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import kz.bitlab.cars_g136.entity.Car;
import kz.bitlab.cars_g136.entity.Category;
import kz.bitlab.cars_g136.entity.Country;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CarSpecification {
    public Specification<Car> getCarSpec(Integer year, String name, Double price, Long countryId
            , Long categoryId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
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
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
