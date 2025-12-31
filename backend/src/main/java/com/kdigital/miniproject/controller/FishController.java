package com.kdigital.miniproject.controller;

import java.util.ArrayList;
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
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class FishController {
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	private final FishDetailRepository fishDRepo;
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getParameterName() + " 파라메터가 누락되었습니다.")
				.build();
		return ResponseEntity.ok().body(res);
	}
	
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
		if(name == null || name.length() == 0)
		{
			res.setSuccess(false);
			res.setError("검색할 이름을 입력해주세요.");
		}
		else
		{
			List<Fish> list =  fishRepo.findByNameContains(name);
			for(Fish f : list)
				res.addData(new FishSimple(f));
		}
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 별 어종 정보
	@PostMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestBody RequestDTO request) throws Exception {
		return responseGetFishsByLocation(request.getNumber());
	}

	@GetMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestParam("location") Long location) throws Exception {
		return responseGetFishsByLocation(location);
	}

	ResponseEntity<Object> responseGetFishsByLocation(Long location) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		if(locRepo.findById(location).isEmpty())
		{
			res.setSuccess(false);
			res.setError("위치 고유번호가 올바르지 않습니다.");
		}
		else
		{
			List<Fish> list = fishRepo.findByLocation(Location.builder().location_no(location).build());
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
		List<String> list =  fishRepo.findFishList();
		for(String name : list) {
			Optional<FishDetail> opt = fishDRepo.findByName(name);
			if(opt.isPresent())
				res.addData(opt.get());
		}
			
		return ResponseEntity.ok().body(res);
	}
	
}
