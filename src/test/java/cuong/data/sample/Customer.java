package cuong.data.sample;

import java.util.Date;
import java.util.Set;


public class Customer {
	private Integer customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String telephone;
	private String password;
	private Integer addressId;
	private Date dateAdded;
	
	private Set<Address> addresses;
	private Set<Order> orders;
	

    public Customer() {
		
	}
	
	public Customer(String firstName, String lastName, String email, String telephone, String password, Date dateAdded) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephone = telephone;
		this.password = password;
		this.dateAdded = dateAdded;
	}
	
	public Set<Address> getAddresses() {
	    return addresses;
	}
	
	public void setAddresses(Set<Address> addresses) {
	    this.addresses = addresses;
	}
	
	public Set<Order> getOrders() {
        return orders;
    }
	
	public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
	
	public Integer getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	
	public Date getDateAdded() {
		return dateAdded;
	}
	
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	@Override
	public String toString() {
		return "Customer [getCustomerId()=" + getCustomerId() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getEmail()=" + getEmail() + ", getTelephone()="
				+ getTelephone() + ", getPassword()=" + getPassword() + ", getAddressId()=" + getAddressId()
				+ ", getDateAdded()=" + getDateAdded() + "]";
	}
	
	
}
