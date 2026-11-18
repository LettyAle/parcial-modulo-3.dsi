package co.edu.unbosque.parcial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.unbosque.parcial.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);

	boolean existsByUsername(String username);
}
