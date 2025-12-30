package com.kdigital.miniproject.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Weather;
import com.kdigital.miniproject.domain.WeatherSimple;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.service.FishService;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FishController {
	private final FishService fishservice;
	private final LocationService locservice;
	private final FishDetailRepository fishDRepo;
	
	public static List<FishSimple> toSimple(List<Fish> list)
	{
		List<FishSimple> ret = new ArrayList<>();
		for(Fish f : list)
			ret.add(new FishSimple(f));
		return ret;
	}
	
	// 어종 이름 검색
	@PostMapping("v1/fish/name")
	public ResponseEntity<Object> getFishs(@RequestBody RequestDTO request) throws Exception {
		return responseGetFishs(request.getText());
	}
	
	@GetMapping("v1/fish/name")
	public ResponseEntity<Object> getFishs(@RequestParam("name")String name) throws Exception {
		return responseGetFishs(name);
	}
	
	ResponseEntity<Object> responseGetFishs(String name) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Fish> list =  fishservice.getFishList(name);
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 별 어종 정보
	@PostMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestBody RequestDTO request) throws Exception {
		return ResponsegetFishsByLocation(request.getNumber());
	}

	@GetMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestParam("location") Long location) throws Exception {
		return ResponsegetFishsByLocation(location);
	}

	ResponseEntity<Object> ResponsegetFishsByLocation(Long location) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		if(locservice.getLocationByNo(location).isEmpty())
		{
			res.setSuccess(false);
			res.setError("위치 고유번호가 올바르지 않습니다.");
		}
		else
		{
			List<Fish> list = fishservice.getFishList(Location.builder().location_no(location).build());
			for(Fish f : list)
				res.addData(new FishSimple(f));
		}
		
		return ResponseEntity.ok().body(res);
	}
	
	// 해당 위치의 날짜의 어종 정보
	@PostMapping("v1/fish/weather")
	public ResponseEntity<Object>  getFishsByWeather(@RequestBody RequestDTO request) throws Exception {
		return responseGetFishsByWeather(request.getText());
	}
	
	@GetMapping("v1/fish/weather")
	public ResponseEntity<Object>  getFishsByWeather(@RequestParam("weather") String date) throws Exception {
		return responseGetFishsByWeather(date);
	}
	
	ResponseEntity<Object>  responseGetFishsByWeather(String date) throws Exception {
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
			List<Fish> list = fishservice.getFishListByDate(date);
			for(Fish f : list)
				res.addData(new FishSimple(f));
		}
		
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("v1/fishlist")
	public ResponseEntity<Object> getFishsList() throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<String> list =  fishservice.findFishList();
		for(String name : list) {
			FishDetail d = fishDRepo.getByName(name);
			if(d != null)
				res.addData(d);
		}
			
		return ResponseEntity.ok().body(res);
	}
	
}
