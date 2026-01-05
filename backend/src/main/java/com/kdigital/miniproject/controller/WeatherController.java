package com.kdigital.miniproject.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherChart;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class WeatherController {
	private final WeatherRepository weaRepo;
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getParameterName() + " 파라메터가 누락되었습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
	
	
	// 날씨 차트
	@PostMapping("/v1/weather/chart")
	public ResponseEntity<Object> postWeatherChart(@RequestBody RequestDTO request) throws Exception {
//		System.out.println("request : " + request.toString());
		return responseWeatherChart(request.getNumber(), request.getText());
	}
	
	@GetMapping("/v1/weather/chart")
	public ResponseEntity<Object> getWeatherChart(@RequestParam("location_no")Long location_no, @RequestParam("query")String query) throws Exception {
//		System.out.println("location_no : " + location_no);
//		System.out.println("query : " + query);
		return responseWeatherChart(location_no, query);
	}
	
	public ResponseEntity<Object> responseWeatherChart(Long location_no, String query) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build(); 
		
		Optional<Location> lOpt = locRepo.findById(location_no);
		if(lOpt.isEmpty()) {
			res.setSuccess(false);
			res.setError("위치정보 오류 입니다.");
		}
		else {
			List<Weather> list = weaRepo.findByLocationChart(location_no);
			for(Weather w : list)
				res.addData(new WeatherChart(w, query));
		}
		
		return ResponseEntity.ok().body(res);
	}
/*	
	// 위치 별 날짜 검색
	@PostMapping("/v1/weather")
	public ResponseEntity<Object> postWeather(@RequestBody RequestDTO request) {
		return responseWeather(request.getNumber(), request.getText());
	}
	
	@GetMapping("/v1/weather")
	public ResponseEntity<Object> getWeather(@RequestParam("location")long location_no, @RequestParam("date")String date) {
		return responseWeather(location_no, date);
	}
	
	ResponseEntity<Object> responseWeather(long location_no, String date) {
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
//			System.out.println(location_no + " : " + d);
//			List<Fish> list = fishRepo.findFishByLocationAndPredcYmd(location_no, date);
//			for(Fish f : list)
//				res.addData(new FishSimple(f));
		}
		return ResponseEntity.ok().body(res);
		
	}
*/
}
