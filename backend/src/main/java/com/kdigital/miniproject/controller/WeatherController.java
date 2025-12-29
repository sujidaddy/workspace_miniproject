package com.kdigital.miniproject.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.service.FishService;
import com.kdigital.miniproject.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherService weaservice;
	private final FishService fishservice;
	
	// 위치 별 날짜 검색
	@PostMapping("/v1/weather")
	public ResponseEntity<Object> getWeather(@RequestBody RequestDTO request) {
		return responseGetWeather(request.getNumber(), request.getText());
	}
	
	@GetMapping("/v1/weather")
	public ResponseEntity<Object> getWeather(@RequestParam("location")long location_no, @RequestParam("date")String date) {
		return responseGetWeather(location_no, date);
	}
	
	ResponseEntity<Object> responseGetWeather(long location_no, String date) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Date d = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			 d = df.parse(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setSuccess(false);
			res.setError("날자 형식이 맞지 않습니다. yyyy-MM-dd");
		}
		
		if(res.isSuccess()) {
			List<Weather> list = weaservice.getWeather(Location.builder().location_no(location_no).build(), d);
			for(Weather w : list)
				res.addData(new WeatherSimple(w, fishservice));
		}
		return ResponseEntity.ok().body(res);
		
	}

}
