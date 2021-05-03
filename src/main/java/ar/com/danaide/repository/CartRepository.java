package ar.com.danaide.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ar.com.danaide.domain.Cart;
import ar.com.danaide.domain.Customer;

/**
 * Spring Data repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query(value = "from Cart c where c.customer= :customer and c.date BETWEEN :startDate AND :endDate")
	List<Cart> findAllByCustomerAndDateStartAndDateEnd(@Param("customer") Customer customer,
			@Param("startDate") LocalDate from, @Param("endDate") LocalDate to);
}
