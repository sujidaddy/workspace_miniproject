package com.kdigital.miniproject.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> getByUsername(String username);
	List<Member> findByUsername(String username);
	List<Member> findByUserid(String userid);
	List<Member> findByEmail(String email);
	Optional<Member> getByGoogle(String google);
	Optional<Member> getByNaver(String naver);
	Optional<Member> getByKakao(String kakao);
}