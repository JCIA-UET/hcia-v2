package uet.jcia.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;

@ManagedBean
@SessionScoped
public class ColumnBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Column column;
	
	public ColumnBean() {
		column = new Column();
	}
	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
	
	@SuppressWarnings("unchecked")
	public void save(Column changeCol){
		System.out.println("Change Col Type:" + changeCol.getType());

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    
	    String sessionid = session.getId();
	    String ssTableKey = sessionid + "table";
	    String ssChgTableKey = sessionid + "chgtable";
	   
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);

		// get table that contains the changed col
		String tableId = changeCol.getTableId();
		
		// Search for all table saved in session
		for(Table t : list) {
			if(t.getTempId().equals(tableId)) {
				// Get column list of the matching table
				List<Column> colsList = t.getListColumn();
				
				// Search for all columns in this table
				for(Column col : colsList) {
					
					// Matching
					if(col.getTempId().equals(changeCol.getTempId())) {
	
						// set properties
						col.setName(changeCol.getName());
						col.setType(changeCol.getType());
						col.setNotNull(changeCol.isNotNull());
						col.setPrimaryKey(changeCol.isAutoIncrement());
						col.setForeignKey(changeCol.isForeignKey());
						col.setAutoIncrement(changeCol.isAutoIncrement());	
					}
				}
				
				
				List<Table> changedList = new ArrayList<>();
				
				if(session.getAttribute(ssChgTableKey) == null) {
					changedList.add(t);
					exContext.getSessionMap().put(ssChgTableKey, changedList);
					//System.out.println("Has no change");
				}
				else {
					changedList = (List<Table>) session.getAttribute(ssChgTableKey);
					//System.out.println("Change List: " + changedList);
					changedList.add(t);
					session.setAttribute(ssChgTableKey, changedList);
					
				}
			}
		}
		session.setAttribute(ssTableKey, list);
	}
}
