package com.kdigital.miniproject;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.kdigital.miniproject.config.PasswordEncoder;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Role;
import com.kdigital.miniproject.persistence.MemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner{
	private final MemberRepository memberRepo;
	private PasswordEncoder encoder = new PasswordEncoder();
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		memberRepo.save(Member.builder()
				.username("admin")
				.password(encoder.encode("abcd"))
				.role(Role.ROLE_ADMIN)
				.enabled(true)
				.build());
	}

}
