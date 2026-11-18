package co.edu.unbosque.parcial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
public class Producto {
	public enum Category {
		FRUIT, VEGETABLE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String name;

	@NotNull
	@Min(0)
	private Integer quantity;

	@NotNull
	@DecimalMin(value = "0.0", inclusive = false)
	@Column(precision = 12, scale = 2)
	private BigDecimal price;

	@NotBlank
	@Size(max = 1000)
	@Pattern(regexp = "https?://.+")
	@Column(length = 1000)
	private String imageUrl;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Category category;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Category getCategory() {
		return category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
