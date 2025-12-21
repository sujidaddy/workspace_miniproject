package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.LoginLog;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long>{

}