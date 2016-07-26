package uet.jcia.controller;


import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Table;

@ManagedBean(name = "tableBean")
@SessionScoped
public class TableBean implements Serializable{
    
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table;
	private List<Table> list;

	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	  
	public List<Table> getList() {
		return list;
	}

	public void setList(List<Table> list) {
		this.list = list;
	}
	
	@SuppressWarnings("unchecked")
	public TableBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    
	    String sessionid = session.getId();
	    String ssTableKey = sessionid + "table";
	    
	    List<Table> list = (List<Table>) session.getAttribute(ssTableKey);
	    setList(list);
	    System.out.println("Table bean: " + list);
	}

	public void save() {
		
	}
    
}