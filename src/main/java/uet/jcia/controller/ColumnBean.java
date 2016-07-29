package uet.jcia.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.MTORelationshipNode;
import uet.jcia.entities.OTMRelationshipNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class ColumnBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String colName;
	private String dataType;
	private String length;
	private boolean nn;
	private boolean pk;
	private boolean fk;
	private boolean ai;
	
	private boolean addSuccess;
	
	private ColumnNode columnNode;
	

	public boolean isAddSuccess() {
		return addSuccess;
	}

	public void setAddSuccess(boolean addSuccess) {
		this.addSuccess = addSuccess;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean isNn() {
		return nn;
	}

	public void setNn(boolean nn) {
		this.nn = nn;
	}

	public boolean isPk() {
		return pk;
	}

	public void setPk(boolean pk) {
		this.pk = pk;
	}

	public boolean isFk() {
		return fk;
	}

	public void setFk(boolean fk) {
		this.fk = fk;
	}

	public boolean isAi() {
		return ai;
	}

	public void setAi(boolean ai) {
		this.ai = ai;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public ColumnNode getColumnNode() {
		return columnNode;
	}

	public void setColumnNode(ColumnNode columnNode) {
		this.columnNode = columnNode;
	}

	public ColumnBean() {
		columnNode = new ColumnNode();
	}

	public void add(ColumnNode col) {
		System.out.println(col);
	}
}
