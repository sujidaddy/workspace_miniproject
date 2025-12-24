package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
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
	public ResponseEntity<Object> getValiedID(@PathVariable String id) throws Exception {
		String ret = memberRepo.findByUserid(id).isPresent() ? "False" : "True";
		System.out.println("validateID result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 이메일 확인
	@GetMapping("/v1/join/validateEmail/{email}")
	public ResponseEntity<Object> getValiedEmail(@PathVariable String email) throws Exception {
		String ret = memberRepo.findByEmail(email).isPresent() ? "False" : "True";
		System.out.println("validateEmail result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 회원가입
	@GetMapping("/v1/join/joinUser")
	public ResponseEntity<Object> getJoinUser(
			HttpServletResponse response,
			@RequestParam("userid") String userid,
			@RequestParam("password") String password,
			@RequestParam("username") String username,
			@RequestParam("email") String email) {
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
		if(memberRepo.findByUserid(userid).isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입한 ID 입니다");
		if(memberRepo.findByEmail(email).isPresent())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 이메일 입니다");
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
