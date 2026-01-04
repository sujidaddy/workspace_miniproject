package com.kdigital.miniproject.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.miniproject.domain.LocationFavorite;
import com.kdigital.miniproject.domain.Member;
import com.kdigital.miniproject.domain.Location;


public interface LocationFavoriteRepository extends JpaRepository<LocationFavorite, Long> {
	List<LocationFavorite> findByMemberAndDeleteTimeIsNull(Member member);
	Optional<LocationFavorite> findByMemberAndLocationAndDeleteTimeIsNull(Member member, Location location);
}
