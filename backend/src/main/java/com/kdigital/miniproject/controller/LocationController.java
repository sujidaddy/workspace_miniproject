package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.LocationLog;
import com.kdigital.miniproject.domain.LocationSimple;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
	private final LocationService locservice;
	private final LocationLogRepository logRepo;
	
	public List<LocationSimple> toSimple(List<Location> list)
	{
		List<LocationSimple> ret = new ArrayList<>();
		for(Location l : list)
			ret.add(new LocationSimple(l));
		return ret;
	}
	
	// 전체 위치 정보 조회
	@GetMapping("/v1/location")
	public List<LocationSimple> getLocations()
	{
		return toSimple(locservice.getLocations());
	}
	
	// 권역별 위치 정보 조회
	@GetMapping("/v1/location/{area}")
	public List<LocationSimple> getLocations(@PathVariable Integer area) throws Exception {
		return toSimple(locservice.getLocationsByArea(Area.builder().area_no(area).build()));
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
