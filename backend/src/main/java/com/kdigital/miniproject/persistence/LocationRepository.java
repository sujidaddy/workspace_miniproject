package com.kdigital.miniproject.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kdigital.miniproject.domain.Location;
import java.util.List;
import java.util.Optional;


public interface LocationRepository extends JpaRepository<Location, Long> {
}
