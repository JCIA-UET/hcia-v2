package uet.jcia.controller;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import uet.jcia.dao.Account;
import uet.jcia.dao.AccountManager;
import uet.jcia.utils.CookieHelper;
import uet.jcia.utils.RandomHelper;

@ManagedBean(name="userBean")
@SessionScoped
public class UserBean {
	private String username;
	private String password;
	
	private AccountManager ac = new AccountManager();
	
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
	
	public void doLogin() {
		System.out.println(username + " " + password);
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    HttpSession session = (HttpSession) ec.getSession(false);
	    String sessionid = session.getId();
		try {
			if (ac.authenticate(username, password)) {
				//HttpSession session = (HttpSession) ec.getSession(true);
				ec.getSessionMap().put(sessionid + "username", username);
				ec.getSessionMap().put(sessionid + "alive", "true");
				
				String code = ac.getAccountCodeByName(username);
				CookieHelper.setCookie("cd", code);
				ec.redirect("index.xhtml");
			}
			else {
				// re log-in or sign-in
				FacesMessage msg = new FacesMessage("Incorrect username or password.");
				FacesContext.getCurrentInstance().addMessage("login-form", msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doLogout() {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    HttpSession session = (HttpSession) ec.getSession(false);
	    String sessionid = session.getId();
	    
	    String username = (String) session.getAttribute(sessionid + "username");
	    String szAlive = (String) session.getAttribute(sessionid + "alive");
	    
	    if (username == null || szAlive == null || szAlive == "false") {
	    	return;
	    }
	    else {
	    	ec.invalidateSession();
	    	CookieHelper.expireCookie("cd");
	    	try {
				ec.redirect("login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public void register() {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    FacesMessage msg = null;
	    
	    Account existAcc = ac.getAccountByUsername(username);
	    
	    if (username == null || password == null || username.equals("") || password.equals("")) {
	    	if (msg == null) {
		    	msg = new FacesMessage("Username and password are required.");
				FacesContext.getCurrentInstance().addMessage("register-form", msg);
	    	}
	    }
	    else if (existAcc != null) {
	    	if (msg == null) {
	    		msg = new FacesMessage("This name is exist. Please choose another name.");
				FacesContext.getCurrentInstance().addMessage("register-form", msg);
	    	}
	    }
	    else {
			Account newAcc = new Account(username, password, RandomHelper.randomString());
			ac.addAccount(newAcc);
			
			try {
				ec.redirect("login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	public void loadExistUser() {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    
	    HttpSession session = (HttpSession) ec.getSession(false);
	    
	    try {
	    	if (session == null) {
		    	ec.redirect("login.xhtml");
		    }
	    	
	    	Cookie codeCookie = CookieHelper.getCookie("cd");
			if (codeCookie == null) {
				ec.redirect("login.xhtml");
				return;
			}
			
			String code = codeCookie.getValue();

			if (code == null) {
				ec.redirect("login.xhtml");
				return;
			}
    			
    		Account acc = ac.getAccountByCode(code);
    		if (acc == null) ec.redirect("login.xhtml");
	    	
    		System.out.println("Found an user: " + acc);
	    	String sessionid = session.getId();
	    	System.out.println("Session ID: " + sessionid);
    		String username = (String) session.getAttribute(sessionid + "username");
    		String szAlive = (String) session.getAttribute(sessionid + "alive");
    		System.out.println("Current session properties: username: " + username + ", szAlive: " + szAlive);
    		System.out.println("Load data: " + acc.getData());
    		if (username == null && szAlive == null) {
    			this.username = username;
	    		ec.getSessionMap().put(sessionid + "username", acc.getUsername());
	    		ec.getSessionMap().put(sessionid + "alive", "true");
	    		ec.getSessionMap().put(sessionid + "data", acc.getData());
    		}
    		else {
    			// do nothing
    		}
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
