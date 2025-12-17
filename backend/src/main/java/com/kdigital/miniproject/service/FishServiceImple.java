package com.kdigital.miniproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Fish;
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
	public List<Fish> getFishList(String name) {
		// TODO Auto-generated method stub
		return fishRepo.findByName(name);
	}

	@Override
	public List<Fish> getFishList(Long location_no, Long weather_no) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
