package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;

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
	public ResponseEntity<Object> validateID(@RequestBody RequestDTO request) throws Exception {
		System.out.println(request);
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByUserid(request.getText()).isPresent();
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
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByEmail(request.getText()).isPresent();
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
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByGoogle(request.getText()).isPresent();
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
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByNaver(request.getText()).isPresent();
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
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.getByKakao(request.getText()).isPresent();
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
	public ResponseEntity<Object> getJoinUser(
			HttpServletResponse response,
			@RequestBody Member member) {
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
			//res.addData(member);
		}
		
		return ResponseEntity.ok(res);
	}
}
