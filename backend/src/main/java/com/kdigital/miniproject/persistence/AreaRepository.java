package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.Area;

public interface AreaRepository extends JpaRepository<Area, Integer> {
}
