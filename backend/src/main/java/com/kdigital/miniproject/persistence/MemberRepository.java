package com.kdigital.miniproject.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Member;
import java.util.List;




public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> getByUsername(String username);
	Optional<Member> getByUserid(String userid);
	Optional<Member> getByEmail(String email);
	Optional<Member> getByGoogle(String google);
	Optional<Member> getByNaver(String naver);
	Optional<Member> getByKakao(String kakao);
}