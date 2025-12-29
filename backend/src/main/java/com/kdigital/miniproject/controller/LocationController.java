package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.LocationLog;
import com.kdigital.miniproject.domain.LocationSimple;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.service.LocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LocationController {

	private final LocationService locservice;
	private final MemberRepository memberRepo;
	private final LocationRepository locRepo;
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
			//res.addData(loc);
			res.addData(new LocationSimple(loc));
		return ResponseEntity.ok().body(res);
	}
	
	// 권역별 위치 정보 조회
	@PostMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestBody RequestDTO request) throws Exception {
		//System.out.println(request.toString());
		return ResponsegetLocations((int)request.getNumber());
	}
	
	@GetMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestParam("area")int area) throws Exception {
		return ResponsegetLocations(area);
	}
	
	ResponseEntity<Object> ResponsegetLocations(int area) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Location> list =  locservice.getLocationsByArea(Area.builder().area_no(area).build());
		for(Location loc : list)
			//res.addData(loc);
			res.addData(new LocationSimple(loc));
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 선택(선택한 적이 있는 위치에 대한 History를 쌓기 위한 로그 만들기
	@PostMapping("/v1/location/select")
	public ResponseEntity<Object> getLocationSelect(@RequestBody RequestDTO request) throws Exception {
		return ResponsegetLocationSelect(request.getNumber(), request.getText());
	}
	
	@GetMapping("/v1/location/select")
	public ResponseEntity<Object> getLocationSelect(@RequestParam("user")long user_no, @RequestParam("location_name")String location_name) throws Exception {
		return ResponsegetLocationSelect(user_no, location_name);
	}
	
	public ResponseEntity<Object> ResponsegetLocationSelect(long user_no, String location_name) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build(); 
		
		Optional<Member> opt = memberRepo.findById(user_no);
		System.out.println(opt);
		Location loc = locRepo.findByName(location_name);
		if(opt.isEmpty()) {
			res.setSuccess(false);
			res.setError("회원정보 오류 입니다.");
		}
		else if(loc == null) {
			res.setSuccess(false);
			res.setError("위치정보 오류 입니다.");
		}
		else {
			Member member = opt.get();
			logRepo.save(LocationLog.builder()
					.member(member)
					.location(loc)
					.selectTime(LocalDateTime.now())
					.build());
			// 요구 데이터 전달
		}
		
		return ResponseEntity.ok().body(res);
	}
	
//	@GetMapping("/v1/location/history")
//	public Page<LocationLog> getLocationHistory(@RequestParam("username") String username) throws Exception {
//		Pageable pageable= PageRequest.of(0, 10, Sort.Direction.DESC, "log_no");
//		return logRepo.findByMember(Member.builder().username(username).build(), pageable);
//	}
	
}
