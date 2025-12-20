package com.kdigital.miniproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.LocationSimple;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
	private final LocationService locservice;
	
	public List<LocationSimple> toSimple(List<Location> list)
	{
		List<LocationSimple> ret = new ArrayList<>();
		for(Location l : list)
			ret.add(new LocationSimple(l));
		return ret;
	}
	
	@GetMapping("/v1/location")
	public List<LocationSimple> getLocations()
	{
		return toSimple(locservice.getLocations());
	}
	
	@GetMapping("/v1/location/{area}")
	public List<LocationSimple> getLocations(@PathVariable Integer area) throws Exception {
		return toSimple(locservice.getLocationsByArea(Area.builder().area_no(area).build()));
	}
	
}
