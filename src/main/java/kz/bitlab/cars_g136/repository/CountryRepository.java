package kz.bitlab.cars_g136.repository;

import kz.bitlab.cars_g136.entity.Country;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
