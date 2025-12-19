package com.kdigital.miniproject.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private AuthenticationSuccessHandler oauth2SuccessHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors->cors.configurationSource(corsSource()));
		// CSRF 보호 비활성화 (CsrfFilter 제거)
		http.csrf(csrf->csrf.disable());
		http.httpBasic(basic->basic.disable());
		
		// 접근권한 설정
		http.authorizeHttpRequests(auth->auth
				.requestMatchers("/member/**").authenticated()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll());	// AuthorizationFilter 등록
	
		http.oauth2Login(oauth2->oauth2.successHandler(oauth2SuccessHandler));
		
		http.formLogin(form->form.loginPage("/system/login").defaultSuccessUrl("/member/main", true));
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
					"http://127.0.0.1:3000",
					"http://10.125.121.*:3000",
					"http://10.125.121.*:5173",
					"http://miniproject.myapp.com:8080",
					"http://miniproject.myapp.com:3000",
					"http://miniproject.myapp.com:5173"));
		config.addAllowedMethod(CorsConfiguration.ALL);		// 요청을 허용할 Method
		config.addAllowedHeader(CorsConfiguration.ALL);		// 요청을 허용할 Header
		config.setAllowCredentials(true);					// 요청/응답에 자격증명정보/쿠키 포함을 허용 여부
		config.addExposedHeader(HttpHeaders.AUTHORIZATION);	// 응답 Header에 Authorization 추가
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
