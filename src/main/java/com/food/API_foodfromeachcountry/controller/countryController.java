package com.food.API_foodfromeachcountry.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.food.API_foodfromeachcountry.exception.ResourceNotFoundException;
import com.food.API_foodfromeachcountry.exception.ValidationException;
import com.food.API_foodfromeachcountry.model.Country;
import com.food.API_foodfromeachcountry.repository.countryRepository;

@RestController
@RequestMapping("/apiFOOD/v1")
public class countryController {
    private final countryRepository countryRepository;

    private final foodController foodController;

    public countryController(countryRepository countryRepository, foodController foodController) {
        this.countryRepository = countryRepository;
        this.foodController=foodController;
    }

    @Transactional(readOnly = true)
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        return new ResponseEntity<List<Country>>(countryRepository.findAll(), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/countries/filterByContinent/{continent}")
    public ResponseEntity<List<Country>> getAllCountriesByContinent(@PathVariable(value = "continent") 
                                                                                        String continent) {
        List<Country> countries;
        if (countryRepository.findCountriesByContinent(continent) != null)
            countries = countryRepository.findCountriesByContinent(continent);
        else
            throw new ResourceNotFoundException("There are not countries registered in this continent");

        return new ResponseEntity<List<Country>>(countries, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/countries")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        existCountryByName(country);
        validateCountry(country);
        return new ResponseEntity<Country>(countryRepository.save(country), HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping("/countries/{id}")
    public String deleteCountry(@PathVariable(value = "id") 
                                                    Long id) {
        Country auxCountry=countryRepository.findByCountryId(id);                                          
        if (auxCountry!= null){
            foodController.deleteFoodByIdCountry(id);
            countryRepository.delete(countryRepository.findByCountryId(id));
        }
        else{
            throw new ResourceNotFoundException("There is not a country registered in the list with id '"+id+"'");
        }
        return auxCountry.getName() + " was deleted";
    }

    @Transactional
    @PutMapping("/countries/{name}")
    public ResponseEntity<Country> modifyCountry(@PathVariable(value = "name") 
                                                String name, @RequestBody Country countryDetails) {
        Country modifyCountry=countryRepository.findByName(name);

        if(modifyCountry!=null){
            modifyCountry.setName(countryDetails.getName());
            modifyCountry.setContinent(countryDetails.getContinent());

            countryRepository.save(modifyCountry);
            return ResponseEntity.ok(modifyCountry);
        }else{
            throw new ResourceNotFoundException("There is not a country registered in the list with name: '"+name+"'");
        }
    }

    private void validateCountry(Country country) {
        if (country.getName() == null || country.getName().trim().isEmpty()) {
            throw new ValidationException("Country's name is obligatory");
        }

        if (country.getContinent() == null || country.getContinent().trim().isEmpty()) {
            throw new ValidationException("Country's continent is obligatory");
        }
    }

    private void existCountryByName(Country country) {
        if (countryRepository.existsCountryByName(country.getName())) {
            throw new ValidationException("Country's name already exists");
        }
    }
}
