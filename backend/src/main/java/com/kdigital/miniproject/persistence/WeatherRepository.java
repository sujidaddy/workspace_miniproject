package com.kdigital.miniproject.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
	List<Weather> findByLocation(Location location);
	List<Weather> findByPredcYmd(LocalDate predcYmd);
	List<Weather> findByLocationAndPredcYmd(Location location, LocalDate predcYmd);
	Optional<Weather> findByLocationAndPredcYmdAndPredcNoonSeCd(Location location, LocalDate predcYmd, String predcNoonSeCd);
	@Query(value="SELECT weather.* FROM weather WHERE location_no = :location_no AND predc_ymd >= curdate() ORDER BY predc_ymd;", nativeQuery=true)
	List<Weather> findByLocationChart(Long location_no);
	
}
