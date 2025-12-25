package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.LoginLog;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class memberJoinController {
	private final MemberRepository memberRepo;
	private final LoginLogRepository loginRepo;
	private PasswordEncoder encoder = new PasswordEncoder();

	// 등록된 ID 확인
	@GetMapping("/v1/join/validateID/{id}")
	public ResponseEntity<Object> validateID(@PathVariable String id) throws Exception {
		String ret = memberRepo.getByUserid(id).isPresent() ? "False" : "True";
		System.out.println("validateID result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 이메일 확인
	@GetMapping("/v1/join/validateEmail/{email}")
	public ResponseEntity<Object> validateEmail(@PathVariable String email) throws Exception {
		String ret = memberRepo.getByEmail(email).isPresent() ? "False" : "True";
		System.out.println("validateEmail result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 구글 계정 확인
	@GetMapping("/v1/join/validateGoogle/{google}")
	public ResponseEntity<Object> validateGoogle(@PathVariable String google) throws Exception {
		String ret = memberRepo.getByGoogle(google).isPresent() ? "False" : "True";
		System.out.println("validateGoogle result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 네이버 계정 확인
	@GetMapping("/v1/join/validateNaver/{naver}")
	public ResponseEntity<Object> validateNaver(@PathVariable String naver) throws Exception {
		String ret = memberRepo.getByNaver(naver).isPresent() ? "False" : "True";
		System.out.println("validateNaver result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 카카오 계정 확인
	@GetMapping("/vi/join/validateKakao/{kakao}")
	public ResponseEntity<Object> validateKakao(@PathVariable String kakao) throws Exception {
		String ret = memberRepo.getByKakao(kakao).isPresent() ? "False" : "True";
		System.out.println("validateKakao result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 회원가입
	@GetMapping("/v1/join/joinUser")
	public ResponseEntity<Object> getJoinUser(
			HttpServletResponse response,
			@RequestParam("userid") String userid,
			@RequestParam("password") String password,
			@RequestParam("username") String username,
			@RequestParam("email") String email,
			@RequestParam("google") String google,
			@RequestParam("naver") String naver,
			@RequestParam("kakao") String kakao) {
		if(memberRepo.getByUserid(userid).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 가입한 ID 입니다");
		if(memberRepo.getByEmail(email).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 가입한 이메일 입니다");
		if(google != null && memberRepo.getByGoogle(google).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 구글계정 입니다");
		if(naver != null && memberRepo.getByNaver(naver).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 네이버계정 입니다");
		if(kakao != null && memberRepo.getByKakao(kakao).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 카카오계정 입니다");
		Member member = Member.builder()
							.userid(userid)
							.password(encoder.encode(password))
							.username(username)
							.email(email)
							.role(Role.ROLE_MEMBER)
							.enabled(true)
							.createTime(LocalDateTime.now())
							.lastLoginTime(LocalDateTime.now())
							.build();
		if(google != null)
			member.setGoogle(google);
		if(naver != null)
			member.setNaver(naver);
		if(kakao != null)
			member.setKakao(kakao);
		memberRepo.save(member);
		System.out.println("getJoinUser result : " + member.getUser_no());
		// 로그인 기록 추가
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		// JWT 생성
		String token = JWTUtil.getJWT(member);
		System.out.println("token : " + token);
		// Cookie에 jwt 추가
		Cookie cookie = new Cookie("jwtToken", token.replaceAll(JWTUtil.prefix, ""));
		cookie.setHttpOnly(true);	// JS에서 접근 못 하게
		cookie.setSecure(false);	// HTTPS에서만 동작
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);		// 60초 * 60 = 1시간
		response.addCookie(cookie);			
			
		return ResponseEntity.ok(member);
	}
}
