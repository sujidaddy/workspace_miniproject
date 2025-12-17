package com.kdigital.miniproject.service;

import com.kdigital.miniproject.domain.Location;

public interface LocationService {
	void insertLocation(Location loc);
	Location getLocation(String name);
}
