package kz.bitlab.cars_g136.service;

import kz.bitlab.cars_g136.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    void addCountry(String name, String code);
}
