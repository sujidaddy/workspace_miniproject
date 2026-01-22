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
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.name LIKE CONCAT('%', :fish_name, '%') AND weather.predc_ymd >= curdate();", nativeQuery=true)
	//Supabase 전환 curdate() => CURRENT_DATE
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.name LIKE CONCAT('%', :fish_name, '%') AND weather.predc_ymd >= CURRENT_DATE;", nativeQuery=true)
	List<Fish> findByName(String fish_name);
	List<Fish> findByLocation(Location loc);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.location_no = :location_no AND weather.predc_ymd >= curdate();", nativeQuery=true)
	//Supabase 전환 curdate() => CURRENT_DATE
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.location_no = :location_no AND weather.predc_ymd >= CURRENT_DATE;", nativeQuery=true)
	List<Fish> findByLocation(long location_no);
	List<Fish> findByWeather(Weather wea);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE predc_ymd = :date;", nativeQuery=true)
	List<Fish> findByDate(String date);
	List<Fish> findByWeatherAndNameContains(Weather wea, String name);
	@Query(value="SELECT distinct name FROM fish WHERE name IS NOT NULL;", nativeQuery=true)
	List<String> findFishList();
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd >= curdate() ORDER BY weather.predc_ymd;", nativeQuery=true)
	// Supabase 전환 curdate() => CURRENT_DATE
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd >= CURRENT_DATE ORDER BY weather.predc_ymd;", nativeQuery=true)
	List<Fish> findFishByLocation(Long location_no);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd = :predcYmd ORDER BY weather.predc_ymd;", nativeQuery=true)
	// Supabase 전환 date와 문자열 비교시 명시적으로 타입 지정 (::DATE)
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON fish.weather_no = weather.weather_no LEFT OUTER JOIN location ON fish.location_no = location.location_no WHERE fish.location_no = :location_no AND weather.predc_ymd = :predcYmd ::DATE ORDER BY weather.predc_ymd;", nativeQuery=true)
	List<Fish> findFishByLocationAndPredcYmd(Long location_no, String predcYmd);
	@Query(value="SELECT fish.* FROM ( SELECT  ROW_NUMBER() OVER(PARTITION BY name ORDER BY max_last_scr DESC) as rn, T_SUB.* FROM ( SELECT  fish.name,  fish.location_no,  MAX(fish.last_scr) as max_last_scr, MAX(fish.fish_no) as fish_no FROM fish  LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no  WHERE weather.predc_ymd >= curdate() GROUP BY fish.name, fish.location_no ) T_SUB ) T LEFT OUTER JOIN fish ON fish.fish_no = T.fish_no  WHERE rn <= :top;", nativeQuery=true)
	// Supabase 전환 curdate() => CURRENT_DATE
	//@Query(value="SELECT fish.* FROM ( SELECT  ROW_NUMBER() OVER(PARTITION BY name ORDER BY max_last_scr DESC) as rn, T_SUB.* FROM ( SELECT  fish.name,  fish.location_no,  MAX(fish.last_scr) as max_last_scr, MAX(fish.fish_no) as fish_no FROM fish  LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no  WHERE weather.predc_ymd >= CURRENT_DATE GROUP BY fish.name, fish.location_no ) T_SUB ) T LEFT OUTER JOIN fish ON fish.fish_no = T.fish_no  WHERE rn <= :top;", nativeQuery=true)
	List<Fish> findFishPointTop(int top);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.location_no = :location_no AND fish.name = :fish_name AND weather.predc_ymd >= curDate() ORDER BY weather.predc_ymd ASC;", nativeQuery=true)
	// Supabase 전환 curdate() => CURRENT_DATE
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.location_no = :location_no AND fish.name = :fish_name AND weather.predc_ymd >= CURRENT_DATE ORDER BY weather.predc_ymd ASC;", nativeQuery=true)
	List<Fish> findByLocationNoAndFishName(long location_no, String fish_name);
	@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.name = :fish_name AND weather.predc_ymd = :predcYmd AND weather.predc_noon_se_cd = :predcNoonSeCd;", nativeQuery=true)
	// Supabase 전환 date와 문자열 비교시 명시적으로 타입 지정 (::DATE)
	//@Query(value="SELECT fish.* FROM fish LEFT OUTER JOIN weather ON weather.weather_no = fish.weather_no WHERE fish.name = :fish_name AND weather.predc_ymd = :predcYmd :: DATE AND weather.predc_noon_se_cd = :predcNoonSeCd;", nativeQuery=true)
	List<Fish> findByNameAndDate(String fish_name, String predcYmd, String predcNoonSeCd);

}
