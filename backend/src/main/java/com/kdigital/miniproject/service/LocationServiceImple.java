package com.kdigital.miniproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Area;
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
	public Location getLocationByName(String name) {
		// TODO Auto-generated method stub
		return locRepo.findByName(name);
	}

	@Override
	public List<Location> getLocations() {
		// TODO Auto-generated method stub
		return locRepo.findAll();
	}

	@Override
	public List<Location> getLocationsByArea(Area area) {
		// TODO Auto-generated method stub
		System.out.println("getLocationsByArea : " + area.toString());
		List<Location> result = locRepo.findByArea(area);
		System.out.println(result.size());
		return result;
	}
	
	
}
