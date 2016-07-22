package uet.jcia.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uet.jcia.entities.Column;

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
	
	public void save(Column changeCol){
		System.out.println("Column Change: " + changeCol);
	}
}
