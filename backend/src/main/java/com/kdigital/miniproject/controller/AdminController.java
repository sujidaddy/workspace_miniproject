package com.kdigital.miniproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.AreaRepository;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;
import com.kdigital.miniproject.service.FetchData;
import com.kdigital.miniproject.service.FetchScheduler;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {
	private final MemberRepository memberRepo;
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	private final WeatherRepository weaRepo;
	private final FishDetailRepository fishDeRepo;
	
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
	
	// 데이터 갱신
	@PostMapping("/v1/admin/fetchData")
	public ResponseEntity<Object> postFetchData(HttpServletRequest request) throws Exception {
		return responseFetchData(request);
	}
	
	@GetMapping("/v1/admin/fetchData")
	public ResponseEntity<Object> getFetchData(HttpServletRequest request) throws Exception {
		return responseFetchData(request);
	}
	
	ResponseEntity<Object> responseFetchData(HttpServletRequest request) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Member member = JWTUtil.parseToken(request, memberRepo);
		if(member == null)
		{
			res.setSuccess(false);
			res.setError("올바르지 않은 정보입니다.");
		}
		else if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
		}
		else
		{
			FetchData data = new FetchData(fishRepo, locRepo, weaRepo, fishDeRepo);
			FetchScheduler scheduler = new FetchScheduler(data);
			scheduler.fetchStart();
		}
		
		return ResponseEntity.ok().body(res);
	}

}
