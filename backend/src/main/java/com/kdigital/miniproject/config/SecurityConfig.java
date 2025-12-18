package com.kdigital.miniproject.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final MemberRepository memberRepo;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors->cors.configurationSource(corsSource()));
		// CSRF 보호 비활성화 (CsrfFilter 제거)
		http.csrf(csrf->csrf.disable());
		
		// 접근권한 설정
		http.authorizeHttpRequests(auth->auth
				.requestMatchers("/member/**").authenticated()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll());	// AuthorizationFilter 등록
		
		http.formLogin(form->form.loginPage("/system/login").defaultSuccessUrl("/board/getBoardList", true));
		http.exceptionHandling(ex->ex.accessDeniedPage("/system/accessDenied"));
		http.logout(logout->logout.invalidateHttpSession(true).logoutSuccessUrl("/"));
		
		
		return http.build();
	}

	private CorsConfigurationSource corsSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(
			Arrays.asList(
					"http://localhost:5173", 
					"http://127.0.0.1:5173", 
					"http://localhost:3000", 
					"http://127.0.0.1:3000"));
		config.addAllowedMethod(CorsConfiguration.ALL);		// 요청을 허용할 Method
		config.addAllowedHeader(CorsConfiguration.ALL);		// 요청을 허용할 Header
		config.setAllowCredentials(true);					// 요청/응답에 자격증명정보/쿠키 포함을 허용 여부
		config.addExposedHeader(HttpHeaders.AUTHORIZATION);	// 응답 Header에 Authorization 추가
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
