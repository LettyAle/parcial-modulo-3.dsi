package co.edu.unbosque.parcial.repository;

import co.edu.unbosque.parcial.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@org.springframework.data.jpa.repository.Query("select p from Product p where p.id = :id")
	Optional<Producto> findByIdForUpdate(@org.springframework.data.repository.query.Param("id") Long id);
}
