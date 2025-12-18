package com.kdigital.miniproject.service;

import java.util.Date;
import java.util.List;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;

public interface WeatherService {
	void insertWeather(Weather loc);
	List<Weather> getWeather(Location loc, Date ymd);
}
