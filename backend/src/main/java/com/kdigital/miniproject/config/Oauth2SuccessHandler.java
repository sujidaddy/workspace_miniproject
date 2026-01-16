package com.kdigital.miniproject.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kdigital.miniproject.domain.LoginLog;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;
import com.kdigital.miniproject.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final MemberRepository memberRepo;
//	private final PasswordEncoder encoder;
	private final LoginLogRepository loginRepo;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Map<String, String> map = getUseInfo(authentication);

		String provider = map.get("provider");
		String name = map.get("name");
		String username = name + "@" + provider;
		
		Optional<Member> find = null;
		switch (provider) {
			case "google":
				find = memberRepo.findByGoogle(username);
				break;
			case "naver":
				find = memberRepo.findByNaver(username);
				break;
			case "kakao":
				find = memberRepo.findByKakao(username);
				break;
		}
		
		Member user = null;
		
		if(find != null && find.isPresent()) {
//			// 기존 로그인 유저
//			System.out.println("기존 로그인 유저");
//			user = find.get();
//			// 최종 접속 시간 처리
//			user.setLastLoginTime(LocalDateTime.now());
//			memberRepo.save(user);
		} else {
//			// 신규 가입
//			System.out.println("신규 가입 유저");
//			user = Member.builder()
//					.username(username)
//					.provider(provider)
//					.email(email)
//					.password(encoder.encode("1a2s3d4f"))
//					.role(Role.ROLE_MEMBER)
//					.enabled(true)
//					// 생성 시간 처리
//					.createTime(LocalDateTime.now())
//					// 최종 접속 시간 처리
//					.lastLoginTime(LocalDateTime.now())
//					.build();
//			memberRepo.save(user);
		}
		// 로그인 기록 추가
		loginRepo.save(LoginLog.builder()
				.member(user)
				.loginTime(LocalDateTime.now())
				.build());
		// JWT 생성
		String token = JWTUtil.getJWT(user);
		//System.out.println("token : " + token);
		// Cookie에 jwt 추가
		Cookie cookie = new Cookie("jwtToken", token.replaceAll(JWTUtil.prefix, ""));
		cookie.setHttpOnly(true);	// JS에서 접근 못 하게
		cookie.setSecure(false);	// HTTPS에서만 동작
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);		// 60초 * 60 = 1시간
		response.addCookie(cookie);
		
		try {
			// 로그인 후 초기 페이지
			response.sendRedirect("http://localhost:3000/system/main");
			//response.sendRedirect("http://localhost:3000/KakaoMap");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	Map<String, String> getUseInfo(Authentication authentication) {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)authentication;
		
		String provider = oAuth2Token.getAuthorizedClientRegistrationId();
		//System.out.println("[OAuth2SuccessHandler]provider: " + provider);
		
		OAuth2User user = (OAuth2User)oAuth2Token.getPrincipal();
		//String email = "unknown";
		String name = "unknown";
		// 로그인 방법별 데이터 구성
		if(provider.equalsIgnoreCase("naver")) {			// naver
			Map<String, Object> response = (Map<String, Object>)user.getAttribute("response");
			name = (String)response.get("name");
			//email = (String)response.get("email");
		} else if(provider.equalsIgnoreCase("google")) {	// google
			name = (String)user.getAttributes().get("name");
			//email = (String)user.getAttributes().get("email");
		} else if(provider.equalsIgnoreCase("kakao")) {		// kakao
			Map<String, String> properties = (Map<String, String>)user.getAttributes().get("properties");  
			name = properties.get("nickname");
		}
		//System.out.println("[OAuth2SuccessHandler]email: " + email);
		return Map.of("provider", provider, "name", name);
	}
}
