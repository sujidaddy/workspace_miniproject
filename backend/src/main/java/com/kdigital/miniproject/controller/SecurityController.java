package com.kdigital.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SecurityController {
	@GetMapping("/system/accessDenied")
	public void accessDenied() {}
	
	@GetMapping("/system/logout")
	public void logout() {}
	
	@GetMapping("/admin/adminPage")
	public void adminPage() {}
	
	@GetMapping("/system/check-ip")
    @ResponseBody // 이 어노테이션이 있어야 IP 주소를 템플릿 파일로 오해하지 않습니다.
    public String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        int port = request.getRemotePort();
        return ip+":"+port; // 이제 "10.125.121.176" 문자열이 화면에 그대로 출력됩니다.
    }
}
