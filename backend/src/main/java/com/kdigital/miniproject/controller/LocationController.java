package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
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

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.LocationLog;
import com.kdigital.miniproject.domain.LocationSimple;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {
	private final LocationRepository locRepo;
	private final MemberRepository memberRepo;
	private final LocationLogRepository logRepo;
	private final FishRepository fishRepo;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getParameterName() + " 파라메터가 누락되었습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
	// 전체 위치 정보 조회
	@GetMapping("/v1/location")
	public ResponseEntity<Object> getLocations()
	{
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Location> list =  locRepo.findAll();
		for(Location loc : list)
			res.addData(new LocationSimple(loc));
		return ResponseEntity.ok().body(res);
	}
	
	// 권역별 위치 정보 조회
	@PostMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestBody RequestDTO request) throws Exception {
		//System.out.println(request.toString());
		return responseGetLocations((int)request.getNumber());
	}
	
	@GetMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestParam("area")int area) throws Exception {
		return responseGetLocations(area);
	}
	
	ResponseEntity<Object> responseGetLocations(int area) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Location> list = locRepo.findByArea(Area.builder().area_no(area).build());
		for(Location loc : list)
			//res.addData(loc);
			res.addData(new LocationSimple(loc));
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 선택(선택한 적이 있는 위치에 대한 History를 쌓기 위한 로그 만들기
	@PostMapping("/v1/location/select")
	public ResponseEntity<Object> getLocationSelect(@RequestBody RequestDTO request) throws Exception {
		return responseGetLocationSelect(request.getNumber());
	}
	
	@GetMapping("/v1/location/select")
	public ResponseEntity<Object> getLocationSelect(@RequestParam("location_no")Long location_no) throws Exception {
		return responseGetLocationSelect(location_no);
	}
	
	public ResponseEntity<Object> responseGetLocationSelect(Long location_no) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build(); 
		
		Optional<Location> lOpt = locRepo.findById(location_no);
		if(lOpt.isEmpty()) {
			res.setSuccess(false);
			res.setError("위치정보 오류 입니다.");
		}
		else {
			List<Fish> list = fishRepo.findFishForecastByLocation(lOpt.get().getLocation_no());
			for(Fish f : list)
				res.addData(new FishSimple(f));
		}
		
		return ResponseEntity.ok().body(res);
	}
	
//	@GetMapping("/v1/location/history")
//	public Page<LocationLog> getLocationHistory(@RequestParam("username") String username) throws Exception {
//		Pageable pageable= PageRequest.of(0, 10, Sort.Direction.DESC, "log_no");
//		return logRepo.findByMember(Member.builder().username(username).build(), pageable);
//	}
	
}
