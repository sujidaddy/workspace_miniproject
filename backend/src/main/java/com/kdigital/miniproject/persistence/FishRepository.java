package com.kdigital.miniproject.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;


public interface FishRepository extends JpaRepository<Fish, Long> {
	Fish findByLocationAndWeatherAndName(Location loc, Weather wea, String name);
	List<Fish> findByNameContains(String name);
	List<Fish> findByLocation(Location loc);
	List<Fish> findByWeather(Weather wea);
	List<Fish> findByLocationAndWeather(Location loc, Weather wea);
	List<Fish> findByLocationAndWeatherAndNameContains(Location loc, Weather wea, String name);

}
