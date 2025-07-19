package kz.bitlab.cars_g136.service.impl;

import kz.bitlab.cars_g136.entity.Country;
import kz.bitlab.cars_g136.repository.CountryRepository;
import kz.bitlab.cars_g136.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public void addCountry(String name, String code) {
        Country country = Country.builder()
                .name(name)
                .code(code)
                .build();
        countryRepository.save(country);
    }
}
