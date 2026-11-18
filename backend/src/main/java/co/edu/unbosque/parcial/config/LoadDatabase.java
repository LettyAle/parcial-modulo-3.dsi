package co.edu.unbosque.parcial.config;

import co.edu.unbosque.parcial.model.Producto;
import co.edu.unbosque.parcial.repository.ProductoRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase implements CommandLineRunner {
	private final ProductoRepository repository;

	public LoadDatabase(ProductoRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... args) {
		if (repository.count() > 0) {
			return;
		}
		add("Manzana roja", 24, "2500", "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=240",
				Producto.Category.FRUIT);
		add("Banano", 30, "1800", "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=240",
				Producto.Category.FRUIT);
		add("Naranja", 20, "2000", "https://images.unsplash.com/photo-1547514701-42782101795e?w=240",
				Producto.Category.FRUIT);
		add("Tomate", 18, "2200", "https://images.unsplash.com/photo-1546470427-e5b89b3b4a32?w=240",
				Producto.Category.VEGETABLE);
		add("Zanahoria", 25, "1500", "https://images.unsplash.com/photo-1445282768818-728615cc910a?w=240",
				Producto.Category.VEGETABLE);
		add("Lechuga", 12, "3500", "https://images.unsplash.com/photo-1556801712-76c8eb07bbc9?w=240",
				Producto.Category.VEGETABLE);
	}

	private void add(String name, int quantity, String price, String imageUrl, Producto.Category category) {
		Producto product = new Producto();
		product.setName(name);
		product.setQuantity(quantity);
		product.setPrice(new BigDecimal(price));
		product.setImageUrl(imageUrl);
		product.setCategory(category);
		repository.save(product);
	}
}
