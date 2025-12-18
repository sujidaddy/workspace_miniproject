package com.kdigital.miniproject.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;


public interface FishRepository extends JpaRepository<Fish, Long> {
	
	List<Fish> findByName(String name);
	List<Fish> findByLocation(Location loc);
	List<Fish> findByWeather(Weather wea);
	List<Fish> findByLocationAndWeather(Location loc, Weather wea);
	Fish findByLocationAndWeatherAndName(Location loc, Weather wea, String name);

}
