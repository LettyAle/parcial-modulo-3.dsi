package co.edu.unbosque.parcial.controller;

import co.edu.unbosque.parcial.dto.PurchaseRequest;
import co.edu.unbosque.parcial.model.Producto;
import co.edu.unbosque.parcial.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductoController {
	private final ProductoService service;

	public ProductoController(ProductoService service) {
		this.service = service;
	}

	@GetMapping
	public List<Producto> list() {
		return service.findAll();
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Producto> create(@Valid @RequestBody Producto product) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(product));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Producto update(@PathVariable Long id, @Valid @RequestBody Producto product) {
		return service.update(id, product);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/purchase")
	@PreAuthorize("hasRole('USER')")
	public Producto purchase(@PathVariable Long id, @Valid @RequestBody PurchaseRequest request) {
		return service.purchase(id, request);
	}
}
