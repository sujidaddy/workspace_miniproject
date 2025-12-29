package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class memberJoinController {
	private final MemberRepository memberRepo;
	private PasswordEncoder encoder = new PasswordEncoder();

	// 등록된 ID 확인
	@PostMapping("/v1/join/validateID")
	public ResponseEntity<Object> validateID(@RequestBody RequestDTO request) throws Exception {
		System.out.println(request);
		return ResponseValidateID(request.getText());
	}
	
	@GetMapping("/v1/join/validateID")
	public ResponseEntity<Object> validateID(@RequestParam("userid")String userid) throws Exception {
		return ResponseValidateID(userid);
	}
	
	ResponseEntity<Object> ResponseValidateID(String userid) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByUserid(userid).isPresent();
			//System.out.println("validateID result : " + validate);
			res.setSuccess(!validate);
			if(validate)
				res.setError("이미 사용중인 ID 입니다.");
		} catch(Exception e) {
			res.setSuccess(false);
			res.setError(e.getMessage());
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 등록된 이메일 확인
	@PostMapping("/v1/join/validateEmail")
	public ResponseEntity<Object> validateEmail(@RequestBody RequestDTO request) throws Exception {
		return ResponseValidateEmail(request.getText());
	}
	
	@GetMapping("/v1/join/validateEmail")
	public ResponseEntity<Object> validateEmail(@RequestParam("email")String email) throws Exception {
		return ResponseValidateEmail(email);
	}
	
	ResponseEntity<Object> ResponseValidateEmail(String email) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByEmail(email).isPresent();
			System.out.println("validateEmail result : " + validate);
			res.setSuccess(!validate);
			if(validate)
				res.setError("이미 사용중인 Email 입니다.");
		} catch(Exception e) {
			res.setSuccess(false);
			res.setError(e.getMessage());
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 등록된 구글 계정 확인
	@PostMapping("/v1/join/validategoogle")
	public ResponseEntity<Object> validateGoogle(@RequestBody RequestDTO request) throws Exception {
		return ResponseValidateGoogle(request.getText());
	}
	
	@GetMapping("/v1/join/validategoogle")
	public ResponseEntity<Object> validateGoogle(@RequestParam("google")String google) throws Exception {
		return ResponseValidateGoogle(google);
	}
	
	ResponseEntity<Object> ResponseValidateGoogle(String google) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByGoogle(google).isPresent();
			res.setSuccess(!validate);
			if(validate)
				res.setError("이미 사용중인 구글 계정 입니다.");
		} catch(Exception e) {
			res.setSuccess(false);
			res.setError(e.getMessage());
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 등록된 네이버 계정 확인
	@PostMapping("/v1/join/validatenaver")
	public ResponseEntity<Object> validateNaver(@RequestBody RequestDTO request) throws Exception {
		return ResponseValidateNaver(request.getText());
	}
	
	@GetMapping("/v1/join/validatenaver")
	public ResponseEntity<Object> validateNaver(@RequestParam("naver")String naver) throws Exception {
		return ResponseValidateNaver(naver);
	}
	
	public ResponseEntity<Object> ResponseValidateNaver(String naver) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByNaver(naver).isPresent();
			//System.out.println("validateID result : " + validate);
			res.setSuccess(!validate);
			if(validate)
				res.setError("이미 사용중인 네이버 계정 입니다.");
		} catch(Exception e) {
			res.setSuccess(false);
			res.setError(e.getMessage());
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 등록된 카카오 계정 확인
	@PostMapping("/v1/join/validatekakao")
	public ResponseEntity<Object> validateKakao(@RequestBody RequestDTO request) throws Exception {
		return ResponseValidateKakao(request.getText());
	}
	
	@GetMapping("/v1/join/validatekakao")
	public ResponseEntity<Object> validateKakao(@RequestParam("kakao")String kakao) throws Exception {
		return ResponseValidateKakao(kakao);
	}
	
	public ResponseEntity<Object> ResponseValidateKakao(String kakao) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByKakao(kakao).isPresent();
			//System.out.println("validateID result : " + validate);
			res.setSuccess(!validate);
			if(validate)
				res.setError("이미 사용중인 카카오 계정 입니다.");
		} catch(Exception e) {
			res.setSuccess(false);
			res.setError(e.getMessage());
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 회원가입
	@PostMapping("/v1/join/joinUser")
	public ResponseEntity<Object> joinUser(
			@RequestBody Member member) {
		//System.out.println(member);
		return ResponseJoinUser(member);
	}
	
	@GetMapping("/v1/join/joinUser")
	public ResponseEntity<Object> joinUser(
			@RequestParam("userid")String userid,
			@RequestParam("password")String password,
			@RequestParam("username")String username,
			@RequestParam("email")String email,
			@RequestParam("google")String google,
			@RequestParam("naver")String naver,
			@RequestParam("kakao")String kakao) {
		return ResponseJoinUser(Member.builder()
								.userid(userid)
								.password(password)
								.username(username)
								.email(email)
								.google(google)
								.naver(naver)
								.kakao(kakao)
								.build());
	}
	
	public ResponseEntity<Object> ResponseJoinUser(Member member) {
		//System.out.println(member);
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		if(memberRepo.getByUserid(member.getUserid()).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 ID 입니다.");
		}
		else if(memberRepo.getByEmail(member.getEmail()).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 Email 입니다.");
		}
		else if(member.getGoogle() != null && memberRepo.getByGoogle(member.getGoogle()).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 구글 계정 입니다.");
		}
		else if(member.getNaver() != null && memberRepo.getByNaver(member.getNaver()).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 네이버 계정 입니다.");
		}
		else if(member.getKakao() != null && memberRepo.getByKakao(member.getKakao()).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 카카오 계정 입니다.");
		}
		else {
			member.setPassword(encoder.encode(member.getPassword()));
			member.setRole(Role.ROLE_MEMBER);
			member.setEnabled(true);
			member.setCreateTime(LocalDateTime.now());
			member.setLastLoginTime(LocalDateTime.now());
			memberRepo.save(member);
			System.out.println("getJoinUser result : " + member.getUser_no());
			res.setSuccess(true);
		}
		
		return ResponseEntity.ok(res);
	}
}
