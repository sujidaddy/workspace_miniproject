package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.FishDetail;



public interface FishDetailRepository extends JpaRepository<FishDetail, Integer> {
	FishDetail getByName(String name);
}
