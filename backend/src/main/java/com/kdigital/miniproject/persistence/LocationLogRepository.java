package com.kdigital.miniproject.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kdigital.miniproject.domain.LocationLog;
import com.kdigital.miniproject.domain.Member;


public interface LocationLogRepository extends JpaRepository<LocationLog, Long>{
	Page<LocationLog> findByMember(Member member, Pageable pageable);

}