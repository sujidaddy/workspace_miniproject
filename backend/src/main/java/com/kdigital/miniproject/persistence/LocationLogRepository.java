package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kdigital.miniproject.domain.LocationLog;

public interface LocationLogRepository extends JpaRepository<LocationLog, Long>{

}