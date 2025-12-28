package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.LoginLog;
import com.kdigital.miniproject.domain.LoginRequestDTO;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
	private final MemberRepository memberRepo;
	private final LoginLogRepository loginRepo;
	private PasswordEncoder encoder = new PasswordEncoder();
	
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
	public ResponseEntity<Object> loginId(
			HttpServletResponse response,
			@RequestBody LoginRequestDTO request) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.getByUserid(request.getId());
		Member member = null;
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("회원 정보가 존재하지 않습니다. ID와 비밀번호를 확인해주세요.");
		}
		else
		{
			member = opt.get();
			if(!encoder.matches(request.getPassword(), member.getPassword()))
			{
				res.setSuccess(false);
				res.setError("회원 정보가 존재하지 않습니다. ID와 비밀번호를 확인해주세요.");
			}
		}
		
		if(member != null) 
		{
			loginRepo.save(LoginLog.builder()
					.member(member)
					.loginTime(LocalDateTime.now())
					.build());
			String token = JWTUtil.getJWT(member);
			res.addData(token);
		}
		
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/logingoogle")
	public ResponseEntity<Object> loginGoogle(
			HttpServletResponse response,
			@RequestBody RequestDTO request) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.getByGoogle(request.getText());
		Member member = null;
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 구글 계정이 아닙니다.");
		}
		else
		{
			member = opt.get();
		}
		
		if(member != null) 
		{
			loginRepo.save(LoginLog.builder()
					.member(member)
					.loginTime(LocalDateTime.now())
					.build());
			String token = JWTUtil.getJWT(member);
			res.addData(token);
		}
		
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/loginnaver")
	public ResponseEntity<Object> loginNaver(
			HttpServletResponse response,
			@RequestBody RequestDTO request) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.getByNaver(request.getText());
		Member member = null;
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 네이버 계정이 아닙니다.");
		}
		else
		{
			member = opt.get();
		}
		
		if(member != null) 
		{
			loginRepo.save(LoginLog.builder()
					.member(member)
					.loginTime(LocalDateTime.now())
					.build());
			String token = JWTUtil.getJWT(member);
			res.addData(token);
		}
		
		return ResponseEntity.ok().body(res);
	}
	
	@PostMapping("/v1/loginkakao")
	public ResponseEntity<Object> loginKakao(
			HttpServletResponse response,
			@RequestBody RequestDTO request) {
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		Optional<Member> opt = memberRepo.getByKakao(request.getText());
		Member member = null;
		if(opt.isEmpty())
		{
			res.setSuccess(false);
			res.setError("가입한 카카오 계정이 아닙니다.");
		}
		else
		{
			member = opt.get();
		}
		
		if(member != null) 
		{
			loginRepo.save(LoginLog.builder()
					.member(member)
					.loginTime(LocalDateTime.now())
					.build());
			String token = JWTUtil.getJWT(member);
			res.addData(token);
		}
		
		return ResponseEntity.ok().body(res);
	}
	
}
