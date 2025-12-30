package com.kdigital.miniproject.service;

import java.util.List;
import java.util.Optional;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Location;

public interface LocationService {
	void insertLocation(Location loc);
	Location getLocationByName(String name);
	Optional<Location> getLocationByNo(Long no);
	List<Location> getLocations();
	List<Location> getLocationsByArea(Area area);
}
