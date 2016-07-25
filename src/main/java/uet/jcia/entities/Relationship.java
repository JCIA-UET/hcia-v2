/* File : Relationship.class 
 * Author : hieusonson9x@gmail.com
 * Details: to express one relationship between two table  
 * */

package uet.jcia.entities;

import java.io.Serializable;

public class Relationship implements Serializable {
    
    private static final long serialVersionUID = 4079820745723632081L;
    
    private Column referColumn;
	private Table referTable;
	private String type;
	
	private String refXml;
	private String tempId;
	private String tableId;
	
	public String getTableId() {
        return tableId;
    }
	
	public void setTableId(String tableId) {
        this.tableId = tableId;
    }
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getRefXml() {
        return refXml;
    }
	
	public void setRefXml(String refXml) {
        this.refXml = refXml;
    }
	
	public String getTempId() {
        return tempId;
    }
	
	public void setTempId(String tempId) {
        this.tempId = tempId;
    }
	
    public Column getReferColumn() {
        return referColumn;
    }

    public void setReferColumn(Column referColumn) {
        this.referColumn = referColumn;
    }

    public Table getReferTable() {
        return referTable;
    }

    public void setReferTable(Table referTable) {
        this.referTable = referTable;
    }

    @Override
    public String toString() {
        return "\n    Relationship ["
                + "referTable=" + referTable.getTableName() + ", referColumn=" + referColumn.getName() 
                + ", type=" + type + ", tempId=" + tempId + ", tableId=" + tableId + "]";
    }
	
}
