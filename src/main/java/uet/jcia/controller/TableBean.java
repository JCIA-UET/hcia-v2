package uet.jcia.controller;


import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uet.jcia.entities.Table;

@ManagedBean(name = "tableBean")
@SessionScoped
public class TableBean implements Serializable{
    
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table;

	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	   
    
}