package com.kdigital.miniproject.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.FishDetail;

public interface FishDetailRepository extends JpaRepository<FishDetail, Integer> {
	Optional<FishDetail> findByName(String name);
	Optional<FishDetail> findByNameAndDetailIsNotNull(String name);
}
