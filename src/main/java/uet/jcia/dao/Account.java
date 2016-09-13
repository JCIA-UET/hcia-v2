package uet.jcia.dao;

/**
 * Account Entity: id, username, password
 * realname, email, type, address 
 * @author cuong
 *
 */
public class Account {
	/**
	 * account id
	 */
	private int id;
	private String username;
	private String password;
	private String data;
	private String code;
	
	public Account(int id, int numOrders) {
		this.id = id;
	}

	public Account(int id, String username, String password, String code) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.code = code;
	}
	
	public Account(String username, String password, String code) {
		this.username = username;
		this.password = password;
		this.code = code;
	}
	
	public Account(int id, String username, String password, String data, String code) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.data = data;
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", password=" + password + ", data=" +  data + "]";
	}
	
}