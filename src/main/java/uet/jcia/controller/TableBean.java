package uet.jcia.controller;


import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Table;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean(name = "tableBean")
@SessionScoped
public class TableBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5764406161579045239L;
	private TreeNode root;
	
	public TableBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    
	    CoreAPI api = new CoreAPI();
	    String sessionid = session.getId();
	    
	    String jsonKey = sessionid + "json";
	    String jsonTree = (String) session.getAttribute(jsonKey);
	    
	}
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public void save() {
		
	}
    
}