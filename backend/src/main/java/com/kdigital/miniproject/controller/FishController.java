package com.kdigital.miniproject.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;

import jakarta.servlet.http.HttpServletRequest;
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
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMismatchParams(MethodArgumentTypeMismatchException ex) {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.error(ex.getName() + " 파라메터의 형식이 올바르지 않습니다.")
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
		return responseFishs(request.getText());
	}
	
	@GetMapping("v1/fish/name")
	public ResponseEntity<Object> getFishs(@RequestParam("name")String name) throws Exception {
		return responseFishs(name);
	}
	
	ResponseEntity<Object> responseFishs(String name) throws Exception {
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
	public ResponseEntity<Object> postFishsByLocation(@RequestBody RequestDTO request) throws Exception {
		return responseFishsByLocation(request.getNumber());
	}

	@GetMapping("v1/fish/location")
	public ResponseEntity<Object> getFishsByLocation(@RequestParam("location") Long location) throws Exception {
		return responseFishsByLocation(location);
	}

	ResponseEntity<Object> responseFishsByLocation(Long location) throws Exception {
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
	
	@PostMapping("v1/fish/weather")
	public ResponseEntity<Object> postFishsByWeather(@RequestBody RequestDTO request) throws Exception {
		return responseFishsByWeather(request.getNumber(), request.getText());
	}

	@GetMapping("v1/fish/weather")
	public ResponseEntity<Object> getFishsByWeather(@RequestParam("location_no") Long location, @RequestParam("weather")String date) throws Exception {
		return responseFishsByWeather(location, date);
	}

	ResponseEntity<Object> responseFishsByWeather(Long location, String date) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			df.parse(date);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res.setSuccess(false);
			res.setError("날자 형식이 맞지 않습니다. yyyy-MM-dd");
		}
		if(locRepo.findById(location).isEmpty())
		{
			res.setSuccess(false);
			res.setError("위치 고유번호가 올바르지 않습니다.");
		}
		else
		{
			List<Fish> list = fishRepo.findFishByLocationAndPredcYmd(location, date);
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
			Optional<FishDetail> opt = fishDRepo.findByNameAndDetailIsNotNull(name);
			if(opt.isPresent())
				res.addData(opt.get());
		}
			
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/fish/modifyDetail")
	public ResponseEntity<Object> postModifyFishDetail(
			HttpServletRequest request,
			@RequestBody FishDetail detail) throws Exception {
		return responseModifyFishDetail(request, detail);
	}
	
	@GetMapping("v1/fish/modifyDetail")
	public ResponseEntity<Object> getModifyFishDetail(
			HttpServletRequest request,
			@RequestParam("data_no") int data_no,
			@RequestParam("name") String name,
			@RequestParam("detail") String detail,
			@RequestParam("url") String url) throws Exception {
		return responseModifyFishDetail(request, FishDetail.builder()
												.data_no(data_no)
												.name(name)
												.detail(detail)
												.url(url)
												.build());
	}
	
	public ResponseEntity<Object> responseModifyFishDetail(
			HttpServletRequest request, FishDetail detail) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		
		if(fishDRepo.findById(detail.getData_no()).isEmpty())
		{
			res.setSuccess(false);
			res.setError("데이터 고유번호가 올바르지 않습니다.");
		}
		else
		{
			fishDRepo.save(detail);
		}
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/fish/removeDetail")
	public ResponseEntity<Object> postRemoveFishDetail(
			HttpServletRequest request,
			@RequestBody FishDetail detail) throws Exception {
		return responseRemoveFishDetail(request, detail);
	}
	
	@GetMapping("v1/fish/removeDetail")
	public ResponseEntity<Object> getRemoveFishDetail(
			HttpServletRequest request,
			@RequestParam("data_no")int data_no) throws Exception {
		return responseRemoveFishDetail(request, FishDetail.builder()
												.data_no(data_no)
												.build());
	}
	
	ResponseEntity<Object> responseRemoveFishDetail(
			HttpServletRequest request,
			FishDetail detail) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		
		if(fishDRepo.findById(detail.getData_no()).isEmpty())
		{
			res.setSuccess(false);
			res.setError("데이터 고유번호가 올바르지 않습니다.");
		}
		else
		{
			fishDRepo.delete(detail);
		}
			
		return ResponseEntity.ok().body(res);
	}
	
}
