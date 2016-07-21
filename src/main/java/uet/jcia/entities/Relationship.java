/* File : Relationship.class 
 * Author : hieusonson9x@gmail.com
 * Details: to express one relationship between two table  
 * */

package uet.jcia.entities;

import java.io.Serializable;

public class Relationship implements Serializable {
    
    private static final long serialVersionUID = 4079820745723632081L;
    
    private String referColumn;
	private String referTable;
	private String referClass;
	private String type;
	
	private String refXml;
	private String tempId;
	
    public String getReferColumn() {
        return referColumn;
    }
	
	public void setReferColumn(String referColumn) {
        this.referColumn = referColumn;
    }
	
	public String getReferTable() {
        return referTable;
    }
	
	public void setReferTable(String referTable) {
        this.referTable = referTable;
    }
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getReferClass() {
        return referClass;
    }
	
	public void setReferClass(String referClass) {
        this.referClass = referClass;
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
	
    @Override
    public String toString() {
        return "\n    Relationship ["
                + "referColumn=" + referColumn + ", referTable=" + referTable + ", referClass=" + referClass
                + ", type=" + type + ", tempId=" + tempId + "]";
    }
	
}
