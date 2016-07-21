package cuong.data.sample;

import java.util.Set;

public class Category {
	private Integer categoryId;
	private Integer parentId;
	private String name;
	private String description;
	
	private Set<Product> products;
	
	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Category() {
		
	}
	
	public Category(Integer parentId, String name, String description) {
		this.parentId = parentId;
		this.name = name;
		this.description = description;
	}
	
	public Category(Integer categoryId, Integer parentId, String name, String description, Set<Product> products) {
		super();
		this.categoryId = categoryId;
		this.parentId = parentId;
		this.name = name;
		this.description = description;
		this.products = products;
	}

	public Category(Integer parentId, String name, String description, Set<Product> products) {
		super();
		this.parentId = parentId;
		this.name = name;
		this.description = description;
		this.products = products;
	}

	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Category [getCategoryId()=" + getCategoryId() + ", getParentId()=" + getParentId() + ", getName()="
				+ getName() + ", getDescription()=" + getDescription() + "]";
	}
	
	
}
