package kz.bitlab.cars_g136.repository;

import kz.bitlab.cars_g136.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findAll(Specification<Car> specification, Pageable pageable);
    List<Car> findAllByYear(int year);

    @Query("select c from Car c " +
            "where lower(c.name) like lower(concat('%', :name,'%'))")
    List<Car> findAllByNameCustom(@Param("name") String name);
//    List<Car> findAllByNameContainsIgnoreCase(String name);

//    List<Car> findAllByYearAndNameContainsIgnoreCase(int year,String name);

    @Query("select c from Car c " +
            "where c.year=:year and c.name like %:name%")
    List<Car> findAllByCustomParam(@Param("year") int year, @Param("name") String name);
}
