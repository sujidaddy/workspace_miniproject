package com.kdigital.miniproject.service;

import java.util.List;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;

public interface FishService {
	void insertFish(Fish fish);
	List<Fish> getFishList(String name);
	List<Fish> getFishList(Location loc);
	List<Fish> getFishList(Weather wea);
	List<Fish> getFishList(Location loc, Weather wea);
	Fish getFish(Location loc, Weather wea, String name);
}
