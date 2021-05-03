package ar.com.danaide.repository;

import ar.com.danaide.domain.SpecialDate;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the SpecialDate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialDateRepository extends JpaRepository<SpecialDate, Long> {
}
