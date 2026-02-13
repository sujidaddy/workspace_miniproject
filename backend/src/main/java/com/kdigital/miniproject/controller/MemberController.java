package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.LoginLog;
import com.kdigital.miniproject.domain.LoginRequestDTO;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class MemberController {
	private final MemberRepository memberRepo;
	private final LoginLogRepository loginRepo;
	private PasswordEncoder encoder = new PasswordEncoder();

	public static class RequestDTO {
		public String google;
		public String naver;
		public String kakao;
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
	
//	public Cookie AddLogAndGetCookie(Member member)
//	{
//		System.out.println(member.toString());
//		loginRepo.save(LoginLog.builder()
//				.member(member)
//				.loginTime(LocalDateTime.now())
//				.build());
//		String token = JWTUtil.getJWT(member);
//		//System.out.println("token : " + token);
//		// Cookie에 jwt 추가
//		Cookie cookie = new Cookie("jwtToken", token.replaceAll(JWTUtil.prefix, ""));
//		cookie.setHttpOnly(true);	// JS에서 접근 못 하게
//		cookie.setSecure(false);	// HTTPS에서만 동작
//		cookie.setPath("/");
//		cookie.setMaxAge(60 * 60);		// 60초 * 60 = 1시간
//		return cookie;
//	}
	
	@PostMapping("/v1/loginid")
	public ResponseEntity<Object> postloginId(
			@RequestBody LoginRequestDTO request) {
		return responseLoginId(request.getUserid(), request.getPassword());
	}
	
	@GetMapping("/v1/loginid")
	public ResponseEntity<Object> getloginId(
			@RequestParam String userid, @RequestParam String password) {
		return responseLoginId(userid, password);
	}
	
	public ResponseEntity<Object> responseLoginId(
			String userid, String password) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.findByUserid(userid);
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("회원 정보가 존재하지 않습니다. ID와 비밀번호를 확인해주세요.");
			return ResponseEntity.ok().body(res);
		}

		Member member = opt.get();
		if(!encoder.matches(password, member.getPassword()))
		{
			res.setSuccess(false);
			res.setError("회원 정보가 존재하지 않습니다. ID와 비밀번호를 확인해주세요.");
			return ResponseEntity.ok().body(res);
		}
		
		member.setLastLoginTime(LocalDateTime.now());
		memberRepo.save(member);
		
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		String token = JWTUtil.getJWT(member);
		res.addData(token);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/logingoogle")
	public ResponseEntity<Object> postLoginGoogle(
			HttpServletResponse response,
			@RequestBody RequestDTO request) {
		return responseLoginGoogle(request.google);
	}
	
	@GetMapping("/v1/logingoogle")
	public ResponseEntity<Object> getLoginGoogle(
			@RequestParam String google) {
		return responseLoginGoogle(google);
	}
	
	public ResponseEntity<Object> responseLoginGoogle(String google) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.findByGoogle(google);
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 구글 계정이 아닙니다.");
			return ResponseEntity.ok().body(res);
		}
		
		Member member = opt.get();
		
		member.setLastLoginTime(LocalDateTime.now());
		memberRepo.save(member);
		
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		String token = JWTUtil.getJWT(member);
		res.addData(token);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/loginnaver")
	public ResponseEntity<Object> postLoginNaver(
			@RequestBody RequestDTO request) {
		return responseLoginNaver(request.naver);
	}
	
	@GetMapping("/v1/loginnaver")
	public ResponseEntity<Object> getLoginNaver(
			@RequestParam String naver) {
		return responseLoginNaver(naver);
	}
	
	public ResponseEntity<Object> responseLoginNaver(String naver) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.findByNaver(naver);
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 네이버 계정이 아닙니다.");
			return ResponseEntity.ok().body(res);
		}
		
		Member member = opt.get();
		
		member.setLastLoginTime(LocalDateTime.now());
		memberRepo.save(member);
		
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		String token = JWTUtil.getJWT(member);
		res.addData(token);
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/loginkakao")
	public ResponseEntity<Object> postLoginKakao(
			@RequestBody RequestDTO request) {
		return responseLoginKakao(request.kakao);
	}
	
	@GetMapping("/v1/loginkakao")
	public ResponseEntity<Object> getLoginKakao(
			@RequestParam String kakao) {
		return responseLoginKakao(kakao);
	}
	
	public ResponseEntity<Object> responseLoginKakao(String kakao) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.findByKakao(kakao);
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 카카오 계정이 아닙니다.");
			return ResponseEntity.ok().body(res);
		}
		
		Member member = opt.get();
		
		member.setLastLoginTime(LocalDateTime.now());
		memberRepo.save(member);
		
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		String token = JWTUtil.getJWT(member);
		res.addData(token);
		return ResponseEntity.ok().body(res);
	}
	
}
