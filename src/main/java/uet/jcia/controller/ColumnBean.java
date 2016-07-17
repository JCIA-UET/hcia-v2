package uet.jcia.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import uet.jcia.entities.Column;

@ManagedBean
@SessionScoped
public class ColumnBean {
	private Column column;

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
}
