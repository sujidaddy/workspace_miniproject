package com.kdigital.miniproject.service;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.persistence.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImple implements LocationService{
	private final LocationRepository locRepo;

	@Override
	public void insertLocation(Location loc) {
		// TODO Auto-generated method stub
		locRepo.save(loc);
	}

	@Override
	public Location getLocation(String name) {
		// TODO Auto-generated method stub
		return locRepo.findByName(name);
	}
	
	
}
