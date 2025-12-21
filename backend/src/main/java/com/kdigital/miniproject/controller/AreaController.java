package com.kdigital.miniproject.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.service.AreaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AreaController {
	private final AreaService areaservice;
	
	// 권역 전체 데이터
	@GetMapping("/v1/area")
	public List<Area> getAreas()
	{
		return areaservice.getAreas();
	}

}
