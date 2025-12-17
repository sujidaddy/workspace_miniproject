package com.kdigital.miniproject.service;

import java.util.List;

import com.kdigital.miniproject.domain.Fish;

public interface FishService {
	void insertFish(Fish fish);
	List<Fish> getFishList(String name);
	List<Fish> getFishList(Long location_no, Long weather_no);
}
