package com.kdigital.miniproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.persistence.FishRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FishServiceImple implements FishService{
	private final FishRepository fishRepo;
	@Override
	public void insertFish(Fish fish) {
		// TODO Auto-generated method stub
		fishRepo.save(fish);
		
	}
	
	@Override
	public Fish getFish(Location loc, Weather wea, String name) {
		// TODO Auto-generated method stub
		return fishRepo.findByLocationAndWeatherAndName(loc, wea, name);
	}

	@Override
	public List<Fish> getFishList(String name) {
		// TODO Auto-generated method stub
		return fishRepo.findByNameContains(name);
	}
	
	@Override
	public List<Fish> getFishList(Location loc) {
		// TODO Auto-generated method stub
		return fishRepo.findByLocation(loc);
	}
	
	@Override
	public List<Fish> getFishList(Weather wea) {
		// TODO Auto-generated method stub
		return fishRepo.findByWeather(wea);
	}
	
	@Override
	public List<Fish> getFishListByDate(String date) {
		// TODO Auto-generated method stub
		return fishRepo.findByDate(date);
	}

	@Override
	public List<Fish> getFishList(Weather wea, String name) {
		// TODO Auto-generated method stub
		return fishRepo.findByWeatherAndNameContains(wea, name);
	}
	
	@Override
	public List<String> findFishList() {
		return fishRepo.findFishList();
	}
	
}
