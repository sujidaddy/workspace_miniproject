package com.kdigital.miniproject.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.FetchLog;

public interface FetchLogRepository extends JpaRepository<FetchLog, Integer> {
	Page<FetchLog> findByErrorMsgIsNull(Pageable pageable);
	Page<FetchLog> findByErrorMsgIsNotNull(Pageable pageable);
}
