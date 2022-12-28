package com.food.API_foodfromeachcountry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.food.API_foodfromeachcountry.model.Country;

@Repository
public interface countryRepository extends JpaRepository<Country, Long>{

    @Query("Select c FROM Country c WHERE c.continent=:continent")
    List<Country> findCountriesByContinent(@Param("continent") String continent);

    @Query("Select c FROM Country c WHERE c.name=:name")
    Country findByName(@Param("name") String name);

    @Query("Select c FROM Country c WHERE c.id=:id")
    Country findByCountryId(@Param("id") Long id);

    // @Query("Delete c FROM Country c WHERE c.id=:id")
    // Void deleteCountryById(@Param("id") Long id);

    boolean existsCountryByName(String name);
}
