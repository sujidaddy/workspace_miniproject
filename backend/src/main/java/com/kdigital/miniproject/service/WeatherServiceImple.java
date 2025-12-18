package com.kdigital.miniproject.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherServiceImple implements WeatherService{
	private final WeatherRepository weaRepo;

	@Override
	public void insertWeather(Weather wea) {
		// TODO Auto-generated method stub
		weaRepo.save(wea);
	}
	
	@Override
	public void updateWeather(Weather wea) {
		// TODO Auto-generated method stub
		weaRepo.save(wea);
	}

	@Override
	public Weather getWeather(Location loc, Date ymd, String predcNoonSeCd) {
		// TODO Auto-generated method stub
		return weaRepo.findByLocationAndPredcYmdAndPredcNoonSeCd(loc, ymd, predcNoonSeCd);
	}
	
	

}
