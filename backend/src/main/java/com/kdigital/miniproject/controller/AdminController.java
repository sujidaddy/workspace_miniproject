package com.kdigital.miniproject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.kdigital.miniproject.domain.FetchLog;
import com.kdigital.miniproject.domain.FishDetail;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.PageDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.FetchLogRepository;
import com.kdigital.miniproject.persistence.FishDetailRepository;
import com.kdigital.miniproject.persistence.FishRepository;
import com.kdigital.miniproject.persistence.LocationRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.persistence.TopLocationRepository;
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
	private final FetchLogRepository logRepo;
	private final TopLocationRepository topRepo;
	
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
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		
		FetchData data = new FetchData(fishRepo, locRepo, weaRepo, fishDeRepo, logRepo, topRepo);
		FetchScheduler scheduler = new FetchScheduler(data);
		scheduler.fetchStart();
		return ResponseEntity.ok().body(res);
	}
	
	@GetMapping("v1/admin/fishlistAll")
	public ResponseEntity<Object> getFishListAll(
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
			res.setError("로그인이 필요합니다.");
			return ResponseEntity.ok().body(res);
		}
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		
		List<String> list =  fishRepo.findFishList();
		for(String name : list) {
			Optional<FishDetail> opt = fishDeRepo.findByName(name);
			if(opt.isPresent())
				res.addData(opt.get());
		}
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/admin/modifyFishDetail")
	public ResponseEntity<Object> postModifyFishDetail(
			HttpServletRequest request,
			@RequestBody FishDetail detail) throws Exception {
		return responseModifyFishDetail(request, detail);
	}
	
	@GetMapping("v1/admin/modifyFishDetail")
	public ResponseEntity<Object> getModifyFishDetail(
			HttpServletRequest request,
			@RequestParam int data_no,
			@RequestParam String name,
			@RequestParam String detail,
			@RequestParam String url) throws Exception {
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
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		if(fishDeRepo.findById(detail.getData_no()).isEmpty())
		{
			res.setSuccess(false);
			res.setError("데이터 고유번호가 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		
		fishDeRepo.save(detail);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/admin/removeFishDetail")
	public ResponseEntity<Object> postRemoveFishDetail(
			HttpServletRequest request,
			@RequestBody FishDetail detail) throws Exception {
		return responseRemoveFishDetail(request, detail);
	}
	
	@GetMapping("v1/admin/removeFishDetail")
	public ResponseEntity<Object> getRemoveFishDetail(
			HttpServletRequest request,
			@RequestParam int data_no) throws Exception {
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
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		if(fishDeRepo.findById(detail.getData_no()).isEmpty())
		{
			res.setSuccess(false);
			res.setError("데이터 고유번호가 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		fishDeRepo.delete(detail);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/admin/memberlist")
	public ResponseEntity<Object> postMemberList(
			HttpServletRequest request,
			@RequestBody  PageDTO<Member> pagedto) throws Exception {
		return responseMemberList(request, pagedto.getPageNo(), pagedto.getNumOfRows());
	}
	
	@GetMapping("v1/admin/memberlist")
	public ResponseEntity<Object> getMemberList(
			HttpServletRequest request,
			@RequestParam int pageNo,
			@RequestParam int numOfRows) throws Exception {
		return responseMemberList(request, pageNo, numOfRows);
	}
	
	public ResponseEntity<Object> responseMemberList(
			HttpServletRequest request,
			int pageNo, int numOfRows) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		pageNo -= 1;
		//System.out.println("pageNo : " + pageNo + ", numOfRows : " + numOfRows);
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
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		
		Pageable pageable = PageRequest.of(pageNo, numOfRows);
		Page<Member> page = memberRepo.findByRole(Role.ROLE_MEMBER, pageable);
		PageDTO<Member> responsePage = new PageDTO<Member>(page);
		res.addData(responsePage);
		return ResponseEntity.ok().body(res);
		
	}
	
	@PostMapping("v1/admin/modifyUserEnabled")
	public ResponseEntity<Object> postModifyUserEnabled(
			HttpServletRequest request,
			@RequestBody Member member) throws Exception {
		return responseModifyUserEnabled(request, member.getUser_no(), member.getEnabled());
	}
	
	@GetMapping("v1/admin/modifyUserEnabled")
	public ResponseEntity<Object> getModifyUserEnabled(
			HttpServletRequest request,
			@RequestParam long user_no,
			@RequestParam boolean enabled) throws Exception {
		return responseModifyUserEnabled(request, user_no, enabled);
	}
	
	ResponseEntity<Object> responseModifyUserEnabled(
			HttpServletRequest request,
			long user_no, boolean enabled) throws Exception {
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
		Optional<Member> opt = memberRepo.findById(user_no);
		if(member == null)
		{
			res.setSuccess(false);
			res.setError("로그인이 필요합니다.");
			return ResponseEntity.ok().body(res);
		}
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("존재하지 않는 회원입니다.");
			return ResponseEntity.ok().body(res);
		}

		Member user = opt.get();
		user.setEnabled(enabled);
		memberRepo.save(user);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("v1/admin/fetchLogList")
	public ResponseEntity<Object> postFetchLogList(
			HttpServletRequest request,
			@RequestBody PageDTO<FetchLog> pagedto) throws Exception {
		//System.out.println(pagedto.toString());
		return responseFetchLogList(request, pagedto.getType(), pagedto.getPageNo(), pagedto.getNumOfRows());
	}
	
	@GetMapping("v1/admin/fetchLogList")
	public ResponseEntity<Object> getFetchLogList(
			HttpServletRequest request,
			@RequestParam String logType,
			@RequestParam int pageNo,
			@RequestParam int numOfRows) throws Exception {
		return responseFetchLogList(request, logType, pageNo, numOfRows);
	}
	
	ResponseEntity<Object> responseFetchLogList(
			HttpServletRequest request,
			String logType,
			int pageNo,
			int numOfRows) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		pageNo -= 1;
		//System.out.println("pageNo : " + pageNo + ", numOfRows : " + numOfRows);
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
		if(member.getRole() != Role.ROLE_ADMIN)
		{
			res.setSuccess(false);
			res.setError("권한이 올바르지 않습니다.");
			return ResponseEntity.ok().body(res);
		}

		Pageable pageable = PageRequest.of(pageNo, numOfRows, Sort.Direction.DESC, "logNo");
		Page<FetchLog> page = null;
		switch(logType)
		{
			case "All":
				page = logRepo.findAll(pageable);
				break;
			case "Success":
				page = logRepo.findByErrorMsgIsNull(pageable);
				break;
			case "Error":
				page = logRepo.findByErrorMsgIsNotNull(pageable);
				break;
			default:
				res.setSuccess(false);
				res.setError("올바른 요청이 아닙니다.");
				return ResponseEntity.ok().body(res);
		}

		PageDTO<FetchLog> responsePage = new PageDTO<FetchLog>(page);
		res.addData(responsePage);
		return ResponseEntity.ok().body(res);
	}

}
