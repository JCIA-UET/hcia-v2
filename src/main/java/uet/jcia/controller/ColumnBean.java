package uet.jcia.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class ColumnBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Column column;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
	
	@SuppressWarnings("unchecked")
	public void save(Column changeCol){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    
	    String sessionid = session.getId();
	    String ssTableKey = sessionid + "table";
	   
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);

		System.out.println("Column Change: " + changeCol);
		
		// get table that contains the changed col
		Table ownTable = changeCol.getTable();
		
		// Search for all table saved in session
		for(Table t : list) {
			if(t == ownTable) {
				// Get column list of the matching table
				List<Column> colsList = t.getListColumn();
				
				// Search for all columns in this table
				for(Column col : colsList) {
					
					// Matching
					if(changeCol.getTempId().equals(col.getTempId())) {
						
						// get index of col in colsList
						int colPosition = colsList.indexOf(col);
						colsList.set(colPosition, changeCol);
					}
				}
			}
		}
		session.setAttribute(ssTableKey, list);
	}
}
