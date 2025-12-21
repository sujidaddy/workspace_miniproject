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
import com.kdigital.miniproject.domain.LoginSession;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.LoginLogRepository;
import com.kdigital.miniproject.persistence.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final MemberRepository memberRepo;
	private final PasswordEncoder encoder;
	private final LoginLogRepository loginRepo;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Map<String, String> map = getUseInfo(authentication);
		
		String username = map.get("provider") + "_" + map.get("email");
		
		Optional<Member> find =  memberRepo.findById(username);
		
		Member user = null;
		
		if(find.isPresent()) {
			// 기존 로그인 유저
			System.out.println("기존 로그인 유저");
			user = find.get();
			// 최종 접속 시간 처리
			user.setLastLoginTime(LocalDateTime.now());
			memberRepo.save(user);
		} else {
			// 신규 가입
			System.out.println("신규 가입 유저");
			user = Member.builder()
					.username(username)
					.password(encoder.encode("1a2s3d4f"))
					.role(Role.ROLE_MEMBER)
					.enabled(true)
					// 생성 시간 처리
					.createTime(LocalDateTime.now())
					// 최종 접속 시간 처리
					.lastLoginTime(LocalDateTime.now())
					.build();
			memberRepo.save(user);
		}
		// 로그인 기록 추가
		loginRepo.save(LoginLog.builder()
				.member(user)
				.loginTime(LocalDateTime.now())
				.build());
		
		// 프런트로 전달할 세션 정보 구성
		HttpSession session = request.getSession();
		LoginSession ls = LoginSession.builder()
				.username(user.getUsername())
				.role(user.getRole())
				.provider(map.get("provider"))
				.build();
		session.setAttribute("user", ls);
		try {
			// 로그인 후 초기 페이지
			response.sendRedirect("http://localhost:8080/member/main");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	Map<String, String> getUseInfo(Authentication authentication) {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)authentication;
		
		String provider = oAuth2Token.getAuthorizedClientRegistrationId();
		System.out.println("[OAuth2SuccessHandler]provider: " + provider);
		
		OAuth2User user = (OAuth2User)oAuth2Token.getPrincipal();
		String email = "unknown";
		// 로그인 방법별 데이터 구성
		if(provider.equalsIgnoreCase("naver")) {			// naver
			email = (String)((Map<String, Object>)user.getAttribute("response")).get("email");
		} else if(provider.equalsIgnoreCase("google")) {	// google
			email = (String)user.getAttributes().get("email");
		} else if(provider.equalsIgnoreCase("kakao")) {		// kakao
			Map<String, String> properties = (Map<String, String>)user.getAttributes().get("properties");  
			email = properties.get("nickname");
		}
		System.out.println("[OAuth2SuccessHandler]email: " + email);
		return Map.of("provider", provider, "email", email);
	}
}
