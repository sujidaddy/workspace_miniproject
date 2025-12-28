package com.kdigital.miniproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.service.AreaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AreaController {
	private final AreaService areaservice;
	
	// 권역 전체 데이터
	@GetMapping("/v1/area")
	public ResponseEntity<Object> getAreas()
	{
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<Area> list = areaservice.getAreas();
		for(Area area : list)
			res.addData(area);
		//System.out.println(res);
		return ResponseEntity.ok().body(res);
	}

}
