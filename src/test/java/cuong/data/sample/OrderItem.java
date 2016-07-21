package cuong.data.sample;

public class OrderItem {
	private Integer orderItemId;
	private String name;
	private String model;
	private Integer quantity;
	private Double price;
	private Double total;
	
	private Order order;
	private Product product;
	public OrderItem() {
		
	}
	
	public OrderItem(String name, String model, Integer quantity, Double price, Double total, Order order,
            Product product) {
        super();
        this.name = name;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.order = order;
        this.product = product;
    }

    public OrderItem(String name, String model, Integer quantity, Double price,
			Double total) {
		super();
		this.name = name;
		this.model = model;
		this.quantity = quantity;
		this.price = price;
		this.total = total;
	}
	public Integer getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Order getOrder() {
        return order;
    }
	
	public void setOrder(Order order) {
        this.order = order;
    }
	
	public Product getProduct() {
        return product;
    }
	
	public void setProduct(Product product) {
        this.product = product;
    }
	
	@Override
	public String toString() {
		return "OrderItem [getOrderItemId()=" + getOrderItemId()
				+ ", getName()=" + getName() + ", getModel()=" + getModel()
				+ ", getQuantity()=" + getQuantity() + ", getPrice()=" + getPrice() + ", getTotal()=" + getTotal()
				+ "]";
	}
	
}
