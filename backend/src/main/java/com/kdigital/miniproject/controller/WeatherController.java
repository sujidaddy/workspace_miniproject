package com.kdigital.miniproject.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherService weaservice;
	
	public List<WeatherSimple> toSimple(List<Weather> list)
	{
		List<WeatherSimple> ret = new ArrayList<>();
		for(Weather w : list)
			ret.add(new WeatherSimple(w));
		return ret;
	}
	
	@GetMapping("/v1/weather/{location}/{date}")
	public List<WeatherSimple> getWeather(@PathVariable Long location, 
			@PathVariable@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) 
	{
		return toSimple(weaservice.getWeather(Location.builder().location_no(location).build(), date));
	}

}
