package com.kdigital.miniproject.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.TopLocationDTO;
import com.kdigital.miniproject.persistence.FetchLogRepository;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.persistence.TopLocationRepository;
import com.kdigital.miniproject.persistence.WeatherRepository;
import com.kdigital.miniproject.service.FetchData;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class FishController {
	private final FishRepository fishRepo;
	private final LocationRepository locRepo;
	private final FishDetailRepository fishDeRepo;
	private final TopLocationRepository topRepo;
	private final WeatherRepository weaRepo;
	private final FetchLogRepository logRepo;
	private final MemberRepository memberRepo;
	
	public static class RequestDTO {
		public String fish_name;
		public long location_no;
		public String weather_date;
	}
	
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
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ext) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(" 허용되지 않는 Method 입니다.")
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
	public ResponseEntity<Object> postFishs(@RequestBody RequestDTO request) throws Exception {
		return responseFishs(request.fish_name);
	}
	
	@GetMapping("v1/fish/name")
	public ResponseEntity<Object> getFishs(@RequestParam String fish_name) throws Exception {
		return responseFishs(fish_name);
	}
	
	ResponseEntity<Object> responseFishs(String fish_name) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		if(fish_name == null || fish_name.length() == 0)
		{
			res.setSuccess(false);
			res.setError("검색할 이름을 입력해주세요.");
			return ResponseEntity.ok().body(res);
		}

		//List<Fish> list =  fishRepo.findByNameContains(fish_name);
		//Supabase 에서 너무 느려서 금일이후의 데이터로 한정
		List<Fish> list = fishRepo.findByName(fish_name);
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	// 위치 별 어종 정보
	@PostMapping("v1/fish/location")
	public ResponseEntity<Object> postFishsByLocation(@RequestBody RequestDTO request) throws Exception {
		return responseFishsByLocation(request.location_no);
	}

	@GetMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestParam Long location_no) throws Exception {
		return responseFishsByLocation(location_no);
	}

	ResponseEntity<Object> responseFishsByLocation(Long location_no) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		if(locRepo.findById(location_no).isEmpty())
		{
			res.setSuccess(false);
			res.setError("위치 고유번호가 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}

		//List<Fish> list = fishRepo.findByLocation(Location.builder().location_no(location_no).build());
		//Supabase 에서 너무 느려서 금일이후의 데이터로 한정
		List<Fish> list = fishRepo.findByLocation(location_no);
		System.out.println();
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/fish/weather")
	public ResponseEntity<Object> postFishsByWeather(@RequestBody RequestDTO request) throws Exception {
		return responseFishsByWeather(request.location_no, request.weather_date);
	}

	@GetMapping("v1/fish/weather")
	public ResponseEntity<Object> getFishsByWeather(@RequestParam Long location_no, @RequestParam String weather_date) throws Exception {
		return responseFishsByWeather(location_no, weather_date);
	}

	ResponseEntity<Object> responseFishsByWeather(Long location_no, String weather_date) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		try {
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			df.parse(date);
			LocalDate.parse(weather_date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			
		} catch (DateTimeParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setSuccess(false);
			res.setError("날자 형식이 맞지 않습니다. yyyy-MM-dd");
			return ResponseEntity.ok().body(res);
		}
		if(locRepo.findById(location_no).isEmpty())
		{
			res.setSuccess(false);
			res.setError("위치 고유번호가 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}

		List<Fish> list = fishRepo.findFishByLocationAndPredcYmd(location_no, weather_date);
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("v1/fishlist")
	public ResponseEntity<Object> getFishsList() throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<String> list =  fishRepo.findFishList();
		for(String name : list) {
			Optional<FishDetail> opt = fishDeRepo.findByNameAndDetailIsNotNull(name);
			if(opt.isPresent())
				res.addData(opt.get());
		}
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/fish/topPoint")
	public ResponseEntity<Object> postFishPointTop() throws Exception {
		return responseFishPointTop();
	}
	
	@GetMapping("v1/fish/topPoint")
	public ResponseEntity<Object> getFishPointTop() throws Exception {
		return responseFishPointTop();
	}
	
	ResponseEntity<Object> responseFishPointTop() throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		List<TopLocationDTO> list =  topRepo.findByCreateDate(LocalDate.now());
		if(list.size() == 0)
		{
			FetchData data = new FetchData(fishRepo, locRepo, weaRepo, fishDeRepo, logRepo, topRepo);
			data.fetchTop3(LocalDate.now());
			list =  topRepo.findByCreateDate(LocalDate.now());
		}
		
		for(TopLocationDTO top : list) {
			res.addData(top);
		}
			
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/fish/topPoint/fishChart")
	public ResponseEntity<Object> postFishsByLocationForChart(
			HttpServletRequest request,
			@RequestBody TopLocationDTO requestdto) {
		//System.out.println(requestdto.toString());
		return responseFishsByLocationForChart(request, requestdto.getLocation_no(), requestdto.getSeafsTgfshNm());
	}
	
	@GetMapping("v1/fish/topPoint/fishChart")
	public ResponseEntity<Object> getFishsByLocationForChart(
			HttpServletRequest request,
			@RequestParam Long location_no,
			@RequestParam String fish_name) {
		return responseFishsByLocationForChart(request, location_no, fish_name);
	}
	
	public ResponseEntity<Object> responseFishsByLocationForChart(
			HttpServletRequest request,
			Long location_no, String fish_name) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		if(JWTUtil.isExpired(request))
		{
			res.setSuccess(false);
			res.setError("토큰이 만료되었습니다.");
			return ResponseEntity.ok().body(res);
		}
		Member member = JWTUtil.parseToken(request, memberRepo);
		if(member == null)
		{
			res.setSuccess(false);
			res.setError("로그인이 필요합니다.");
			return ResponseEntity.ok().body(res);
		}
		
		List<Fish> list = fishRepo.findByLocationNoAndFishName(location_no, fish_name);
		//System.out.println("location_name : " + location_name);
		//System.out.println("fish_name : " + fish_name);
		//System.out.println("size : " + list.size());
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
}
