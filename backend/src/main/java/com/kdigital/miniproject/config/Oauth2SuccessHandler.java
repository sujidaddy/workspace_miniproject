package com.kdigital.miniproject.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.MemberRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final MemberRepository memberRepo;
	private final PasswordEncoder encoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		Map<String, String> map = getUseInfo(authentication);
		
		String username = map.get("provider") + "_" + map.get("email");
		
		memberRepo.save(Member.builder()
						.username(username)
						.password(encoder.encode("1a2s3d4f"))
						.role(Role.ROLE_MEMBER)
						.enabled(true)
						.build());
		try {
			response.sendRedirect("http://miniproject.myapp.com:8080/member/main");
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
