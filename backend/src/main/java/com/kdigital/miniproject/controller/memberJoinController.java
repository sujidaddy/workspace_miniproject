package com.kdigital.miniproject.controller;

import java.time.LocalDateTime;

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

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.ModifyMemberDTO;
import com.kdigital.miniproject.domain.RequestDTO;
import com.kdigital.miniproject.domain.ResponseDTO;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class memberJoinController {
	private final MemberRepository memberRepo;
	private PasswordEncoder encoder = new PasswordEncoder();
	
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

	// 등록된 ID 확인
	@PostMapping("/v1/join/validateID")
	public ResponseEntity<Object> postValidateID(@RequestBody RequestDTO request) throws Exception {
		System.out.println(request);
		return responseValidateID(request.getText());
	}
	
	@GetMapping("/v1/join/validateID")
	public ResponseEntity<Object> getValidateID(@RequestParam String userid) throws Exception {
		return responseValidateID(userid);
	}
	
	ResponseEntity<Object> responseValidateID(String userid) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.findByUserid(userid).isPresent();
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
	public ResponseEntity<Object> postValidateEmail(@RequestBody RequestDTO request) throws Exception {
		return responseValidateEmail(request.getText());
	}
	
	@GetMapping("/v1/join/validateEmail")
	public ResponseEntity<Object> getValidateEmail(@RequestParam String email) throws Exception {
		return responseValidateEmail(email);
	}
	
	ResponseEntity<Object> responseValidateEmail(String email) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.findByEmail(email).isPresent();
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
	public ResponseEntity<Object> postValidateGoogle(@RequestBody RequestDTO request) throws Exception {
		return responseValidateGoogle(request.getText());
	}
	
	@GetMapping("/v1/join/validategoogle")
	public ResponseEntity<Object> postValidateGoogle(@RequestParam String google) throws Exception {
		return responseValidateGoogle(google);
	}
	
	ResponseEntity<Object> responseValidateGoogle(String google) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.findByGoogle(google).isPresent();
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
	public ResponseEntity<Object> postValidateNaver(@RequestBody RequestDTO request) throws Exception {
		return responseValidateNaver(request.getText());
	}
	
	@GetMapping("/v1/join/validatenaver")
	public ResponseEntity<Object> getValidateNaver(@RequestParam String naver) throws Exception {
		return responseValidateNaver(naver);
	}
	
	public ResponseEntity<Object> responseValidateNaver(String naver) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.findByNaver(naver).isPresent();
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
	public ResponseEntity<Object> postValidateKakao(@RequestBody RequestDTO request) throws Exception {
		return responseValidateKakao(request.getText());
	}
	
	@GetMapping("/v1/join/validatekakao")
	public ResponseEntity<Object> getValidateKakao(@RequestParam String kakao) throws Exception {
		return responseValidateKakao(kakao);
	}
	
	public ResponseEntity<Object> responseValidateKakao(String kakao) throws Exception {
		ResponseDTO res = ResponseDTO.builder()
				.success(false)
				.build();
		try {
			boolean validate = memberRepo.findByKakao(kakao).isPresent();
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
	public ResponseEntity<Object> postJoinUser(
			@RequestBody Member member) {
		//System.out.println(member);
		return responseJoinUser(member);
	}
	
	@GetMapping("/v1/join/joinUser")
	public ResponseEntity<Object> getJoinUser(
			@RequestParam String userid,
			@RequestParam String password,
			@RequestParam String username,
			@RequestParam String email,
			@RequestParam String google,
			@RequestParam String naver,
			@RequestParam String kakao) {
		return responseJoinUser(Member.builder()
								.userid(userid)
								.password(password)
								.username(username)
								.email(email)
								.google(google)
								.naver(naver)
								.kakao(kakao)
								.build());
	}
	
	boolean validateId(String userid) {
		String regex = "^[a-zA-Z0-9_]{7,16}$";
		return userid.matches(regex);
	}
	
	boolean validatePassword(String password) {
		return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,20}$");
	}
	
	boolean validateName(String username) {
		return username.matches("^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]{3,20}$");
	}
	
	boolean validateMail(String email) {
		return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	}
	
	public ResponseEntity<Object> responseJoinUser(Member member) {
		//System.out.println(member);
		ResponseDTO res = ResponseDTO.builder()
				.success(true)
				.build();
		String userid = member.getUserid(); 
		String password = member.getPassword();
		String username = member.getUsername();
		String email = member.getEmail();
		String google = member.getGoogle();
		String naver = member.getNaver();
		String kakao = member.getKakao();
		if(userid == null || userid.length() == 0
				|| password == null || password.length() == 0
				|| username == null || username.length() == 0
				|| email == null || email.length() == 0)
		{
			res.setSuccess(false);
			res.setError("누락된 데이터가 있습니다.");
		}
		else if(!validateId(userid))
		{
			res.setSuccess(false);
			res.setError("아이디는 7~16자의 영문, 숫자, 밑줄(_)만 사용 가능합니다.");
		}
		else if(!validatePassword(password))
		{
			res.setSuccess(false);
			res.setError("비밀번호는 10~20자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.");
		}
		else if(!validateName(username))
		{
			res.setSuccess(false);
			res.setError("이름은 3~20자의 한글 또는 영문으로만 구성되어야 합니다.");
		}
		else if(!validateMail(email))
		{
			res.setSuccess(false);
			res.setError("유효하지 않은 이메일 주소입니다.");
		}
		else if(memberRepo.findByUserid(userid).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 ID 입니다.");
		}
		else if(memberRepo.findByEmail(email).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 Email 입니다.");
		}
		else if(google != null && memberRepo.findByGoogle(google).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 구글 계정 입니다.");
		}
		else if(naver != null && memberRepo.findByNaver(naver).isPresent())
		{
			res.setSuccess(false);
			res.setError("이미 사용중인 네이버 계정 입니다.");
		}
		else if(kakao != null && memberRepo.findByKakao(kakao).isPresent())
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
			//System.out.println("JoinUser result : " + member.getUser_no());
			res.setSuccess(true);
		}
		
		return ResponseEntity.ok(res);
	}
	
	// 정보수정
	@PostMapping("/v1/join/modifyUser")
	public ResponseEntity<Object> postModifyUser(
			HttpServletRequest request,
			@RequestBody ModifyMemberDTO modifyMember) {
		System.out.println(modifyMember);
		return responseModifyUser(request, modifyMember);
	}
	
	@GetMapping("/v1/join/modifyUser")
	public ResponseEntity<Object> getModifyUser(
			HttpServletRequest request,
			@RequestParam String currentPassword,
			@RequestParam String newPassword,
			@RequestParam String newUsearname) {
		ModifyMemberDTO member = new ModifyMemberDTO();
		member.setCurrentPassword(currentPassword);
		member.setNewPassword(newPassword);
		member.setNewUsername(newUsearname);
		return responseModifyUser(request, member);
	}
	
	ResponseEntity<Object> responseModifyUser(
			HttpServletRequest request,
			ModifyMemberDTO modifyMember) {
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
		}
		else if(!validatePassword(modifyMember.getNewPassword()))
		{
			res.setSuccess(false);
			res.setError("비밀번호는 10~20자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.");
		}
		else if(!validateName(modifyMember.getNewUsername()))
		{
			res.setSuccess(false);
			res.setError("이름은 3~20자의 한글 또는 영문으로만 구성되어야 합니다.");
		}
		else if(!encoder.matches(modifyMember.getCurrentPassword(), member.getPassword()))
		{
			res.setSuccess(false);
			res.setError("올바르지 않은 정보입니다.");
		}
		else
		{
			member.setPassword(encoder.encode(modifyMember.getNewPassword()));
			member.setUsername(modifyMember.getNewUsername());
			memberRepo.save(member);
			System.out.println("ModifyUser result : " + member.getUser_no());
			res.setSuccess(true);
		}
		return ResponseEntity.ok(res);
	}
}
