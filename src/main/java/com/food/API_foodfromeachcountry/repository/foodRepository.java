package com.food.API_foodfromeachcountry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.food.API_foodfromeachcountry.model.Food;

public interface foodRepository extends JpaRepository<Food, Long>{
    
    @Query("Select f FROM Food f WHERE f.country.name=:name")
    List<Food> findByCountryName(@Param("name") String name);

    @Query("Select f FROM Food f WHERE f.country.continent=:continent")
    List<Food> findByContinent(@Param("continent") String continent);

    @Query("Select f FROM Food f WHERE f.name=:name")
    Food findByName(@Param("name") String name);

    @Query("Select f FROM Food f WHERE f.country.id=:id")
    List<Food> findByCountryId(@Param("id") Long id);

    boolean existsByName(String name);
}
