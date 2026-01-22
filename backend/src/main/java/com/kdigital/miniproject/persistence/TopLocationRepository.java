package com.kdigital.miniproject.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.TopLocationDTO;



public interface TopLocationRepository extends JpaRepository<TopLocationDTO, Long> {
	List<TopLocationDTO> findByCreateDate(LocalDate createDate);
}
