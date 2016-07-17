package uet.jcia.controller;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uet.jcia.entities.Table;

@ManagedBean(name = "tableBean")
@SessionScoped
public class TableBean {
    
   private Table table;

	public Table getTable() {
		return table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	   
    
}