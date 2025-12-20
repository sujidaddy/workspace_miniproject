package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;
import java.util.List;
import java.util.Date;


public interface WeatherRepository extends JpaRepository<Weather, Long> {
	List<Weather> findByLocation(Location location);
	List<Weather> findByPredcYmd(Date predcYmd);
	List<Weather> findByLocationAndPredcYmd(Location location, Date predcYmd);
	Weather findByLocationAndPredcYmdAndPredcNoonSeCd(Location location, Date predcYmd, String predcNoonSeCd);
	
}
