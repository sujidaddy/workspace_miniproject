package com.kdigital.miniproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherChart;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class WeatherController {
	private final WeatherRepository weaRepo;
	private final LocationRepository locRepo;
	
	public static class RequestDTO {
		public long location_no;
		public String query;
		public String toString() {return location_no + ", " + query;}
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getParameterName() + " 파라메터가 누락되었습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMismatchParams(MethodArgumentTypeMismatchException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getName() + " 파라메터의 형식이 올바르지 않습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ext) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(" 허용되지 않는 Method 입니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
	// 날씨 차트
	@PostMapping("/v1/weather/chart")
	public ResponseEntity<Object> postWeatherChart(@RequestBody RequestDTO request) throws Exception {
		//System.out.println("request : " + request.toString());
		return responseWeatherChart(request.location_no, request.query);
	}
	
	@GetMapping("/v1/weather/chart")
	public ResponseEntity<Object> getWeatherChart(@RequestParam Long location_no, @RequestParam String query) throws Exception {
//		System.out.println("location_no : " + location_no);
//		System.out.println("query : " + query);
		return responseWeatherChart(location_no, query);
	}
	
	public ResponseEntity<Object> responseWeatherChart(Long location_no, String query) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		boolean invalidquery = false;
		switch(query) {
		case "파고":
		case "풍속":
		case "수온":
		case "유속":
			invalidquery = true;
			break;
		}
		Optional<Location> lOpt = locRepo.findById(location_no);
		if(!invalidquery)
		{
			res.setSuccess(false);
			res.setError("올바른 요청이 아닙니다.");
			return ResponseEntity.ok().body(res);
		}
		if(lOpt.isEmpty()) {
			res.setSuccess(false);
			res.setError("위치정보 오류 입니다.");
			return ResponseEntity.ok().body(res);
		}

		List<Weather> list = weaRepo.findByLocationChart(location_no);
		for(Weather w : list)
			res.addData(new WeatherChart(w, query));
		return ResponseEntity.ok().body(res);
	}
}
