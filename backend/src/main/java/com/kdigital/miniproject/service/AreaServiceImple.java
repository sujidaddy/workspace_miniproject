package com.kdigital.miniproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.persistence.AreaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AreaServiceImple implements AreaService{
	private final AreaRepository areaRepo;

	@Override
	public List<Area> getAreas() {
		// TODO Auto-generated method stub
		return areaRepo.findAll();
	}
}
