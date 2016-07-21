package cuong.data.sample;

import java.util.Date;
import java.util.Set;

public class Product {
    private Integer productId;
    private Integer quantity;
    private Integer status;
    
    private Double price;
    
    private String name;
    private String description;
    private String model;
    private String image;
    
    private Date addedDate;
    private Date modifiedDate;
    
    private Category category;
    private Set<OrderItem> orderItems;
    
    public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Product() {
    }

	public Product(Integer quantity, Integer status, Double price, String name, String description, String model,
            String image, Date addedDate, Date modifiedDate) {
        super();
        this.quantity = quantity;
        this.status = status;
        this.price = price;
        this.name = name;
        this.description = description;
        this.model = model;
        this.image = image;
        this.addedDate = addedDate;
        this.modifiedDate = modifiedDate;
    }

    public Product(Integer quantity, Integer status, Double price, String name, String description, String model,
			String image, Date addedDate, Date modifiedDate, Category category) {
		super();
		this.quantity = quantity;
		this.status = status;
		this.price = price;
		this.name = name;
		this.description = description;
		this.model = model;
		this.image = image;
		this.addedDate = addedDate;
		this.modifiedDate = modifiedDate;
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "Product [productId=" + productId + ", quantity=" + quantity + ", status=" + status + ", price=" + price
                + ", name=" + name + ", description=" + description + ", model=" + model + ", image=" + image
                + ", addedDate=" + addedDate + ", modifiedDate=" + modifiedDate + ", category=" + category
                + ", orderItems=" + orderItems + "]";
    }

}
