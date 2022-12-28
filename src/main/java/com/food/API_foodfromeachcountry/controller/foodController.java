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
import com.food.API_foodfromeachcountry.model.Food;
import com.food.API_foodfromeachcountry.repository.countryRepository;
import com.food.API_foodfromeachcountry.repository.foodRepository;


@RestController
@RequestMapping("/apiFOOD/v1")
public class foodController {
    private final foodRepository foodRepository;

    private final countryRepository countryRepository; 

    public foodController(foodRepository foodRepository, countryRepository countryRepository) {
        this.foodRepository = foodRepository;
        this.countryRepository=countryRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping("/foods")
    public ResponseEntity<List<Food>> getAllFoods() {
        return new ResponseEntity<List<Food>>(foodRepository.findAll(), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/foods/filterByCountryId/{id}")
    public ResponseEntity<List<Food>> getAllFoodsByCountryId(@PathVariable(value="id") 
                                                                                Long id) {
        List<Food> foods;
        if(countryRepository.existsById(id)){
            foods=foodRepository.findByCountryId(id);
        }else{
            throw new ResourceNotFoundException("There is not a country registered in the list with Id '"+id+"'");
        }
        
        return new ResponseEntity<List<Food>>(foods, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/foods/filterByContinentName/{continentName}")
    public ResponseEntity<List<Food>> getAllFoodsByContinentName(@PathVariable(value="continentName") 
                                                                                String continentName) {
        List<Food> foodsOfContinent;
        if(countryRepository.findCountriesByContinent(continentName)!=null){
            foodsOfContinent=foodRepository.findByContinent(continentName);
        }else{
            throw new ResourceNotFoundException("There is not a country registered in the list called '"+continentName+"'");
        }
        
        return new ResponseEntity<List<Food>>(foodsOfContinent, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/countries/{id}/foods")
    public ResponseEntity<Food> createFood(
            @PathVariable(value = "id") Long countryId,
            @RequestBody Food food) {
        if(countryRepository.findByCountryId(countryId)!=null){
            Country countryAux=countryRepository.findByCountryId(countryId);
            food.setCountry(countryAux);
            food.setName(food.getName());
            food.setPrice(food.getPrice());
            validateFood(food);
            existFoodByName(food);
        }else{
            throw new ResourceNotFoundException("There is not a country registered in the list with id '"+ countryId+"'");
        }
        
        return new ResponseEntity<Food>(foodRepository.save(food), HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping("/foods/{name}")
    public String deleteFood(@PathVariable(value = "name") 
                                                    String name) {
        if (foodRepository.findByName(name) == null)
            throw new ResourceNotFoundException("There is not a food registered in the list called '"+name+"'");
        else
            foodRepository.delete(foodRepository.findByName(name));
        return name + " was deleted";
    }

    public void deleteFoodByIdCountry(Long idCountry){
        if(foodRepository.findByCountryId(idCountry)!=null){
            List<Food> foodListAux=foodRepository.findByCountryId(idCountry);
            foodRepository.deleteAllInBatch(foodListAux);
        }else{
            throw new ResourceNotFoundException("No food registered in country's id '"+idCountry+"'");
        }
    }

    @Transactional
    @PutMapping("/foods/{name}")
    public ResponseEntity<Food> modifyFood(@PathVariable(value = "name") 
                                                String name, @RequestBody Food foodDetails) {
        if(foodRepository.findByName(name)!=null){
            Food modifyFood=foodRepository.findByName(name);
            modifyFood.setName(foodDetails.getName());
            // modifyFood.se(countryRepository.findByCountryId(foodDetails.getCountry().getId()));
            modifyFood.setPrice(foodDetails.getPrice());
            foodRepository.save(modifyFood);
            return ResponseEntity.ok(modifyFood);
        }else{
            throw new ResourceNotFoundException("There is not a food registered in the list with name: '"+name+"'");
        }
    }

    private void validateFood(Food food) {
        if (food.getName() == null || food.getName().trim().isEmpty()) {
            throw new ValidationException("Food's name is obligatory");
        }

        if (food.getCountry().getId() == null || food.getCountry().getId().toString().trim().isEmpty()) {
            throw new ValidationException("Food's id country is obligatory");
        }

        if (food.getPrice() == null || food.getPrice().trim().isEmpty()) {
            throw new ValidationException("Food's price is obligatory");
        }
    }

    private void existFoodByName(Food food) {
        if (foodRepository.existsByName(food.getName())) {
            throw new ValidationException("Country's name already exists");
        }
    }
}
