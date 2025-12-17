package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kdigital.miniproject.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String>{

}