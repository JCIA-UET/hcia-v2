package cuong.data.sample;

import java.util.Date;
import java.util.Set;

public class Order {
	private Integer orderId;
	private String firstName;
	private String lastName;
	private String telephone;
	private String address;
	private String comment;
	private Integer status;
	private Double total;
	private Date dateAdded;
	private Date dateModified;

	private Customer customer;
	private Set<OrderItem> orderItems;
	
	public Order() {
		
	}
	
	public Order(String firstName, String lastName, String telephone, String address, String comment, Integer status,
            Double total, Date dateAdded, Date dateModified, Customer customer) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.address = address;
        this.comment = comment;
        this.status = status;
        this.total = total;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.customer = customer;
    }

    public Order(String firstName, String lastName, String telephone, String address,
			String comment, Integer status, Double total, Date dateAdded, Date dateModified) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.telephone = telephone;
		this.address = address;
		this.comment = comment;
		this.status = status;
		this.total = total;
		this.dateAdded = dateAdded;
		this.dateModified = dateModified;
	}
	
	public Set<OrderItem> getOrderItems() {
        return orderItems;
    }
	
	public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Double getTotal() {
		return total;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}
	
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public Date getDateModified() {
		return dateModified;
	}
	
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public Customer getCustomer() {
        return customer;
    }
	
	public void setCustomer(Customer customer) {
        this.customer = customer;
    }
	
	@Override
	public String toString() {
		return "Order [getOrderId()=" + getOrderId() + ", getFirstName()="
				+ getFirstName() + ", getLastName()=" + getLastName() + ", getTelephone()=" + getTelephone()
				+ ", getAddress()=" + getAddress() + ", getComment()=" + getComment() + ", getStatus()=" + getStatus()
				+ ", getTotal()=" + getTotal() + ", getDateAdded()=" + getDateAdded() + ", getDateModified()="
				+ getDateModified() + "]";
	}
}
