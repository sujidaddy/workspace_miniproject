package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
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

import com.kdigital.miniproject.domain.Area;
import com.kdigital.miniproject.domain.Fish;
import com.kdigital.miniproject.domain.FishSimple;
import com.kdigital.miniproject.domain.Location;
import com.kdigital.miniproject.domain.LocationFavorite;
import com.kdigital.miniproject.domain.LocationFavoriteSimple;
import com.kdigital.miniproject.domain.LocationLog;
import com.kdigital.miniproject.domain.LocationSimple;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationFavoriteRepository;
import com.kdigital.miniproject.persistence.LocationLogRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
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
	private final LocationFavoriteRepository favoriteRepo;
	
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
	public ResponseEntity<Object> postLocations(@RequestBody RequestDTO request) throws Exception {
		//System.out.println(request.toString());
		return responseLocations((int)request.getNumber());
	}
	
	@GetMapping("/v1/location/area")
	public ResponseEntity<Object> getLocations(@RequestParam("area")int area) throws Exception {
		return responseLocations(area);
	}
	
	ResponseEntity<Object> responseLocations(int area) throws Exception {
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
	public ResponseEntity<Object> postLocationSelect(@RequestBody RequestDTO request) throws Exception {
		return responseLocationSelect(request.getNumber());
	}
	
	@GetMapping("/v1/location/select")
	public ResponseEntity<Object> getLocationSelect(@RequestParam("location_no")Long location_no) throws Exception {
		return responseLocationSelect(location_no);
	}
	
	public ResponseEntity<Object> responseLocationSelect(Long location_no) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build(); 
		
		Optional<Location> lOpt = locRepo.findById(location_no);
		if(lOpt.isEmpty()) {
			res.setSuccess(false);
			res.setError("위치정보 오류 입니다.");
			return ResponseEntity.ok().body(res);
		}

		logRepo.save(LocationLog.builder()
						.location(lOpt.get())
						.selectTime(LocalDateTime.now())
						.build());
		List<Fish> list = fishRepo.findFishByLocation(lOpt.get().getLocation_no());
		for(Fish f : list)
			res.addData(new FishSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/location/favorite")
	public ResponseEntity<Object> postLocationFavorite(
			HttpServletRequest request) throws Exception {
		return responseLocationFavorite(request);
	}
	
	@GetMapping("/v1/location/favorite")
	public ResponseEntity<Object> getLocationFavorite(
			HttpServletRequest request) throws Exception {
		return responseLocationFavorite(request);
	}
	
	ResponseEntity<Object> responseLocationFavorite(
			HttpServletRequest request) throws Exception {
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
			res.setError("올바르지 않은 정보입니다.");
			return ResponseEntity.ok().body(res);
		}

		res.setSuccess(true);
		List<LocationFavorite> list = favoriteRepo.findByMemberAndDeleteTimeIsNull(member);
		for(LocationFavorite f : list)
			res.addData(new LocationFavoriteSimple(f));
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/location/favorite/add")
	public ResponseEntity<Object> postLocationFavoriteAdd(
			HttpServletRequest request,
			@RequestBody RequestDTO requestdto) throws Exception {
		return responseLocationFavoriteAdd(request, requestdto.getNumber());
	}
	
	@GetMapping("/v1/location/favorite/add")
	public ResponseEntity<Object> getLocationFavoriteAdd(
			HttpServletRequest request,
			@RequestParam("location_no")long location) throws Exception {
		return responseLocationFavoriteAdd(request, location);
	}
	
	ResponseEntity<Object> responseLocationFavoriteAdd(
			HttpServletRequest request,
			long location) throws Exception {
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
			res.setError("올바르지 않은 정보입니다.");
			return ResponseEntity.ok().body(res);
		}

		Optional<LocationFavorite> opt = favoriteRepo.findByMemberAndLocationAndDeleteTimeIsNull(member, Location.builder().location_no(location).build());
		if(opt.isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 등록된 즐겨찾기입니다.");
			return ResponseEntity.ok().body(res);
		}

		res.setSuccess(true);
		LocationFavorite favorite = LocationFavorite.builder()
				.member(member)
				.location(Location.builder().location_no(location).build())
				.createTime(LocalDateTime.now())
				.build();
		favoriteRepo.save(favorite);
		return ResponseEntity.ok().body(res);
	}	
	
	@PostMapping("/v1/location/favorite/remove")
	public ResponseEntity<Object> postLocationFavoriteRemove(
			HttpServletRequest request,
			@RequestBody RequestDTO requestdto) throws Exception {
		return responseLocationFavoriteRemove(request, requestdto.getNumber());
	}
	
	@GetMapping("/v1/location/favorite/remove")
	public ResponseEntity<Object> getLocationFavoriteRemove(
			HttpServletRequest request,
			@RequestParam("location_no")long location) throws Exception {
		return responseLocationFavoriteAdd(request, location);
	}
	
	ResponseEntity<Object> responseLocationFavoriteRemove(
			HttpServletRequest request,
			long location) throws Exception {
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
			res.setError("올바르지 않은 정보입니다.");
			return ResponseEntity.ok().body(res);
		}

		Optional<LocationFavorite> opt = favoriteRepo.findByMemberAndLocationAndDeleteTimeIsNull(member, Location.builder().location_no(location).build());
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("존재하지 않는 즐겨찾기입니다.");
			return ResponseEntity.ok().body(res);
		}

		res.setSuccess(true);
		LocationFavorite favorite = opt.get();
		favorite.setDeleteTime(LocalDateTime.now());
		favoriteRepo.save(favorite);
		return ResponseEntity.ok().body(res);
	}
}
