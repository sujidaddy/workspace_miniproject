package com.kdigital.miniproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
// 로그인 후 메인 페이지
@RequestMapping("/member/")
public class MemberController {
	@GetMapping("/main")
	public void main() {}
	
}
