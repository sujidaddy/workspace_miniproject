package com.kdigital.miniproject.service;

import java.util.Date;
import java.util.List;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.Weather;

public interface WeatherService {
	void insertWeather(Weather wea);
	void updateWeather(Weather wea);
	Weather getWeather(Location loc, Date ymd, String predcNoonSeCd);
}
