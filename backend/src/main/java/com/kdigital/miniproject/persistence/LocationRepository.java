package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Location;
import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {
	Location findByName(String name);
	List<Location> findByArea(Area area);
}
