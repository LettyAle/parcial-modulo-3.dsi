package co.edu.unbosque.parcial.service;

import co.edu.unbosque.parcial.dto.PurchaseRequest;
import co.edu.unbosque.parcial.model.Producto;
import co.edu.unbosque.parcial.repository.ProductoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {
	private final ProductoRepository repository;

	public ProductoService(ProductoRepository repository) {
		this.repository = repository;
	}

	public List<Producto> findAll() {
		return repository.findAll(org.springframework.data.domain.Sort.by("category", "name"));
	}

	@Transactional
	public Producto create(Producto product) {
		return repository.save(product);
	}

	@Transactional
	public Producto update(Long id, Producto changes) {
		Producto product = find(id);
		product.setName(changes.getName());
		product.setQuantity(changes.getQuantity());
		product.setPrice(changes.getPrice());
		product.setImageUrl(changes.getImageUrl());
		product.setCategory(changes.getCategory());
		return repository.save(product);
	}

	@Transactional
	public void delete(Long id) {
		repository.delete(find(id));
	}

	@Transactional
	public Producto purchase(Long id, PurchaseRequest request) {
		Optional<Producto> result = repository.findByIdForUpdate(id);
		if (result.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
		}
		Producto product = result.get();
		if (product.getQuantity() < request.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay suficientes unidades disponibles");
		}
		product.setQuantity(product.getQuantity() - request.getQuantity());
		return repository.save(product);
	}

	private Producto find(Long id) {
		Optional<Producto> result = repository.findById(id);
		if (result.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
		}
		return result.get();
	}
}
