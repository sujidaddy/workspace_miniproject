package com.kdigital.miniproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.service.FishService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FishController {
	private final FishService fishservice;
	
	public static List<FishSimple> toSimple(List<Fish> list)
	{
		List<FishSimple> ret = new ArrayList<>();
		for(Fish f : list)
			ret.add(new FishSimple(f));
		return ret;
	}
	
	// 어종 이름 검색
	@GetMapping("v1/fish/{name}")
	public List<Fish> getFishs(@PathVariable String name) throws Exception {
		return fishservice.getFishList(name);
	}
	
	// 위치 별 어종 정보
	@GetMapping("v1/fish/location")
	public List<Fish> getFishsByLocation(@RequestParam("location") Long location) throws Exception {
		return fishservice.getFishList(Location.builder().location_no(location).build());
	}
	
	// 해당 위치의 날짜의 어종 정보
	@GetMapping("v1/fish/weather")
	public List<Fish> getFishsByWeather(@RequestParam("weather") Long weather) throws Exception {
		return fishservice.getFishList(Weather.builder().weather_no(weather).build());
	}
	
	@GetMapping("v1/fish/{name}/")
	public List<Fish> getFishsByNameAndLocationAndWeather(@PathVariable String name, @RequestParam("weather") Long weather) throws Exception {
		return fishservice.getFishList(Weather.builder().weather_no(weather).build(), name);
	}
	
}
