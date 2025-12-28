package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@PostMapping("/v1/join/validateID")
	public ResponseEntity<Object> validateID(@RequestBody String id) throws Exception {
		String ret = memberRepo.getByUserid(id).isPresent() ? "False" : "True";
		System.out.println("validateID result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 이메일 확인
	@PostMapping("/v1/join/validateEmail")
	public ResponseEntity<Object> validateEmail(@RequestBody String email) throws Exception {
		String ret = memberRepo.getByEmail(email).isPresent() ? "False" : "True";
		System.out.println("validateEmail result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 구글 계정 확인
	@PostMapping("/v1/join/validategoogle")
	public ResponseEntity<Object> validateGoogle(@RequestBody String google) throws Exception {
		String ret = memberRepo.getByGoogle(google).isPresent() ? "False" : "True";
		System.out.println("validateGoogle result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 네이버 계정 확인
	@PostMapping("/v1/join/validatenaver")
	public ResponseEntity<Object> validateNaver(@RequestBody String naver) throws Exception {
		String ret = memberRepo.getByNaver(naver).isPresent() ? "False" : "True";
		System.out.println("validateNaver result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 등록된 카카오 계정 확인
	@PostMapping("/v1/join/validatekakao")
	public ResponseEntity<Object> validateKakao(@RequestBody String kakao) throws Exception {
		String ret = memberRepo.getByKakao(kakao).isPresent() ? "False" : "True";
		System.out.println("validateKakao result : " + ret);
		return ResponseEntity.ok(ret);
	}
	
	// 회원가입
	@PostMapping("/v1/join/joinUser")
	public ResponseEntity<Object> getJoinUser(
			HttpServletResponse response,
			@RequestBody Member member) {
		System.out.println(member);
		if(memberRepo.getByUserid(member.getUserid()).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 가입한 ID 입니다");
		if(memberRepo.getByEmail(member.getEmail()).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 가입한 이메일 입니다");
		if(member.getGoogle() != null && memberRepo.getByGoogle(member.getGoogle()).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 구글계정 입니다");
		if(member.getNaver() != null && memberRepo.getByNaver(member.getNaver()).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 네이버계정 입니다");
		if(member.getKakao() != null && memberRepo.getByKakao(member.getKakao()).isPresent())
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 인증한 카카오계정 입니다");
		member.setPassword(encoder.encode(member.getPassword()));
		member.setRole(Role.ROLE_MEMBER);
		member.setEnabled(true);
		member.setCreateTime(LocalDateTime.now());
		member.setLastLoginTime(LocalDateTime.now());
		memberRepo.save(member);
		System.out.println("getJoinUser result : " + member.getUser_no());
		// 로그인 기록 추가
		loginRepo.save(LoginLog.builder()
				.member(member)
				.loginTime(LocalDateTime.now())
				.build());
		return ResponseEntity.ok(member);
	}
}
