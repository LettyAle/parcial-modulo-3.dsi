package co.edu.unbosque.parcial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.parcial.model.Producto;
import jakarta.persistence.LockModeType;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@org.springframework.data.jpa.repository.Query("select p from Producto p where p.id = :id")
	Optional<Producto> findByIdForUpdate(@org.springframework.data.repository.query.Param("id") Long id);
}
