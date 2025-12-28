package com.kdigital.miniproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
	private final LocationService locservice;
	private final LocationLogRepository logRepo;
	
	// 전체 위치 정보 조회
	@GetMapping("/v1/location")
	public ResponseEntity<Object> getLocations()
	{
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Location> list =  locservice.getLocations();
		for(Location loc : list)
			res.addData(loc);
		return ResponseEntity.ok().body(res);
	}
	
	// 권역별 위치 정보 조회
	@PostMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestBody RequestDTO request) throws Exception {
		System.out.println(request.toString());
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Location> list =  locservice.getLocationsByArea(Area.builder().area_no(request.getNumber()).build());
		for(Location loc : list)
			res.addData(loc);
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 선택(선택한 적이 있는 위치에 대한 History를 쌓기 위한 로그 만들기
	@GetMapping("/v1/location/select")
	public List<Fish> getLocationSelect(@RequestParam("username") String username, @RequestParam("location") Long location) throws Exception {
//		logRepo.save(LocationLog.builder()
//						.member(Member.builder().username(username).build())
//						.location(Location.builder().location_no(location).build())
//						.selectTime(LocalDateTime.now())
//						.build());
		return null;
	}
	
//	@GetMapping("/v1/location/history")
//	public Page<LocationLog> getLocationHistory(@RequestParam("username") String username) throws Exception {
//		Pageable pageable= PageRequest.of(0, 10, Sort.Direction.DESC, "log_no");
//		return logRepo.findByMember(Member.builder().username(username).build(), pageable);
//	}
	
}
