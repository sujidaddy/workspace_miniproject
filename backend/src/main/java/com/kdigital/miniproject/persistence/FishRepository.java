package com.kdigital.miniproject.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;

public interface FishRepository extends JpaRepository<Fish, Long> {
	Optional<Fish> findByLocationAndWeatherAndName(Location loc, Weather wea, String name);
	List<Fish> findByNameContains(String name);
	List<Fish> findByLocation(Location loc);
	List<Fish> findByWeather(Weather wea);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE predc_ymd = :date;", nativeQuery=true)
	List<Fish> findByDate(String date);
	List<Fish> findByWeatherAndNameContains(Weather wea, String name);
	@Query(value="SELECT distinct name FROM fish WHERE name IS NOT NULL;", nativeQuery=true)
	List<String> findFishList();
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd >= curdate() ORDER BY weather.predc_ymd;", nativeQuery=true)
	List<Fish> findFishByLocation(Long location_no);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd = :predcYmd ORDER BY weather.predc_ymd;", nativeQuery=true)
	List<Fish> findFishByLocationAndPredcYmd(Long location_no, String predcYmd);
	@Query(value="SELECT * FROM (SELECT ROW_NUMBER() OVER(PARTITION BY name ORDER BY last_scr DESC) as rn, fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE weather.predc_ymd >= curdate()) T WHERE rn <= :top;", nativeQuery=true)
	List<Fish> findFishPointTop(int top);

}
