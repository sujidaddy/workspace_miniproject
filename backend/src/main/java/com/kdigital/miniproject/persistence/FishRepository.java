package com.kdigital.miniproject.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;


public interface FishRepository extends JpaRepository<Fish, Long> {
	Fish findByLocationAndWeatherAndName(Location loc, Weather wea, String name);
	List<Fish> findByNameContains(String name);
	List<Fish> findByLocation(Location loc);
	List<Fish> findByWeather(Weather wea);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE predc_ymd = :date;", nativeQuery=true)
	List<Fish> findByDate(String date);
	List<Fish> findByWeatherAndNameContains(Weather wea, String name);
	@Query(value="SELECT distinct name FROM fish WHERE name IS NOT NULL;", nativeQuery=true)
	List<String> findFishList();

}
