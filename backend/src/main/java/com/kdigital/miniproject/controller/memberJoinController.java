package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	// 등록된 ID 확인
	@GetMapping("/v1/join/validID/{id}")
	public String getValiedID(@PathVariable String id) throws Exception {
		String ret = memberRepo.findByUserid(id).size() == 0 ? "True" : "False";
		return ret;
	}
	
	// 등록된 이름 확인
	@GetMapping("/v1/join/validName/{name}")
	public String getValiedName(@PathVariable String name) throws Exception {
		String ret = memberRepo.findByUsername(name).size() == 0 ? "True" : "False";
		return ret;
	}
	
	// 등록된 이메일 확인
	@GetMapping("/v1/join/validEmail/{email}")
	public String getValiedEmail(@PathVariable String email) throws Exception {
		String ret = memberRepo.findByEmail(email).size() == 0 ? "True" : "False";
		return ret;
	}
	
	// 회원가입
	@GetMapping("/v1/join/joinUser")
	public Member getJoinUser(
			HttpServletResponse response,
			@RequestParam("userid") String userid,
			@RequestParam("password") String password,
			@RequestParam("username") String username,
			@RequestParam("email") String email) {
		Member member = Member.builder()
							.userid(userid)
							.password(password)
							.username(username)
							.email(email)
							.role(Role.ROLE_MEMBER)
							.enabled(true)
							.createTime(LocalDateTime.now())
							.lastLoginTime(LocalDateTime.now())
							.build();
		memberRepo.save(member);
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
			
		return member;
	}
}
