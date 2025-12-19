package com.kdigital.miniproject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
	private final LocationService locservice;
	
	@GetMapping("/location")
	public List<Location> getLocations()
	{
		return locservice.getLocations();
	}
	
	@GetMapping("/location/{area}")
	public List<Location> getLocations(@PathVariable Integer area) throws Exception {
		return locservice.getLocationsByArea(Area.builder().area_no(area).build());
	}
	
}
