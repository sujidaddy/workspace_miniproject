package com.kdigital.miniproject.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByUsername(String username);
	Optional<Member> findByUserid(String userid);
	Optional<Member> findByEmail(String email);
	Optional<Member> findByGoogle(String google);
	Optional<Member> findByNaver(String naver);
	Optional<Member> findByKakao(String kakao);
}