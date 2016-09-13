package uet.jcia.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import uet.jcia.utils.RandomHelper;

/**
 * Manager for Account entity
 * @author cuong
 *
 */
public class AccountManager {
	
	DBConnector dbconnector = DBConnector.getInstance();
	
	Connection conn = null;
	
	/**
	 * get account from db by username
	 * @param username
	 * @return account entity
	 */
	public Account getAccountByUsername(String username){
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.username = ?";
		PreparedStatement pts = null;
		Account account = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setString(1, username);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				int accountId = rs.getInt(1);
				String password = rs.getString(3);
				String code = rs.getString(5);
				
				account = new Account(accountId, username, password, code);
			}
			
			con.close();
			return account;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * get account by id
	 * @param accountId account's id
	 * @return the account which owns that id
	 */
	public Account getAccountById(int accountId){
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.accountId = ?;";
		PreparedStatement pts = null;
		Account account = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setInt(1, accountId);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				String username = rs.getString(2);
				String password = rs.getString(3);
				String code = rs.getString(5);
				
				account = new Account(accountId, username, password, code);

			}
			
			con.close();
			return account;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAccountDataById(int accountId) {
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.id = ?;";
		PreparedStatement pts = null;
		String data = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setInt(1, accountId);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				data = rs.getString(4);
			}
			
			con.close();
			return data;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAccountCodeById(int id) {
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.id = ?;";
		PreparedStatement pts = null;
		String code = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setInt(1, id);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				code = rs.getString(5);
			}
			
			con.close();
			return code;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getAccountCodeByName(String name) {
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.username = ?;";
		PreparedStatement pts = null;
		String code = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setString(1, name);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				code = rs.getString(5);
			}
			
			con.close();
			return code;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Account getAccountByCode(String code) {
		ResultSet rs = null ;
		String sqlcommand = "select * from account where account.code = ?;";
		PreparedStatement pts = null;
		Account acc = null;
		try {
			Connection con = dbconnector.createConnection();
			pts = con.prepareStatement(sqlcommand);
			pts.setString(1, code);
			rs = pts.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String data = rs.getString(4);
				
				acc = new Account(id, username, password, data, code);
			}
			
			con.close();
			return acc;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void setDataToAccount(String data, Account acc) {
		String sqlCommand = "UPDATE account Set data= ? WHERE id = ?; ";
		PreparedStatement pst ; 
		conn = dbconnector.createConnection();
		try {
			pst = conn.prepareStatement(sqlCommand);
			pst.setString(1, data);
			pst.setInt(2, acc.getId());
			pst.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * authenticate user
	 * @param username input username
	 * @param password input password
	 * @return account if user's account is valid
	 * false if otherwise
	 */
	public boolean authenticate(String username , String password){
		Account account = getAccountByUsername(username);
		if (account == null ||
			!account.getPassword().equals(password)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * add new Account into db
	 * @param newAccount the new account object
	 * @return account id if success, 0 if fail
	 */
	public int addAccount(Account newAccount){
		String sqlCommand = "insert into account (username, password, code) values (?, ?, ?)";
		PreparedStatement pst = null;
		int id = 0;
		
		try {
			conn = dbconnector.createConnection();
			pst = conn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, newAccount.getUsername());
			pst.setString(2, newAccount.getPassword());
			pst.setString(3, newAccount.getCode());
			
			pst.executeUpdate();
			
			// get generate key
			ResultSet pKeys = pst.getGeneratedKeys();
			if (pKeys.next()) {
				id = pKeys.getInt(1);
			}
			
			conn.close();
			return id;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	public boolean removeAccountByUsername(String username){
 		return true;
	}
//	public boolean updatePassword(int id, Account account){
//		String sqlCommand = "UPDATE account Set password= ? WHERE accountId = ?; ";
//		PreparedStatement pst ; 
//		conn = dbconnector.createConnection();
//		try {
//			pst = conn.prepareStatement(sqlCommand);
//			pst.setString(1, account.getPassword());
//			pst.setInt(2, id);
//			pst.executeUpdate();
//			return true;
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//
//	}

//	public boolean updateEmployee (int id, Account account){
//		String sqlCommand = "UPDATE account Set username=?, password=? WHERE accountId = ?; ";
//		PreparedStatement pst ; 
//		conn = dbconnector.createConnection();
//		try {
//			pst = conn.prepareStatement(sqlCommand);
//			pst.setString(1, account.getUsername());
//			pst.setString(2, account.getPassword());
//			pst.setInt(3, id);
//			pst.executeUpdate();
//			return true;
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public List<Account> getAllAccounts(){
		List<Account> list = new ArrayList<>();
		String sqlCommand =
				"select * from account";
		PreparedStatement pst ; 
		ResultSet rs = null ; 
		conn = dbconnector.createConnection();
		try {
			pst = conn.prepareStatement(sqlCommand);
			rs = pst.executeQuery();
			while(rs.next()){
				int accountId = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String code = rs.getString(5);
		
				Account account = new Account(accountId, username, password, code);
				list.add(account);
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	
	public boolean removeAccountById(int id){
		if (getAccountById(id) == null) {
			return false;
		}
		try {
			conn = dbconnector.createConnection();
			String query = "delete from account " +
							"where accountId= ?";
			
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, id);
			statement.execute();

			conn.close();
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
}

	