package com.kdigital.miniproject.config;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.kdigital.miniproject.domain.Member;

public class SecurityUser extends User{
	private Member member;
	
	public SecurityUser(Member member) {
		super(member.getUsername(), member.getPassword(), AuthorityUtils.createAuthorityList(member.getRole().toString()));
		this.member = member;
	}
	
	public Member getMember() {
		return member;
	}

}
