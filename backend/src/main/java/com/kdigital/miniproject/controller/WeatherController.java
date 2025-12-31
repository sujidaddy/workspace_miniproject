package com.kdigital.miniproject.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherRepository weaRepo;
	private final FishRepository fishRepo;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getParameterName() + " 파라메터가 누락되었습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
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
			List<Weather> list = weaRepo.findByLocationAndPredcYmd(Location.builder().location_no(location_no).build(), d);
			for(Weather w : list)
				res.addData(new WeatherSimple(w, fishRepo));
		}
		return ResponseEntity.ok().body(res);
		
	}

}
