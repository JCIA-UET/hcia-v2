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
	
	private String hbmTag;
	private String mappingName;
	
	public String getHbmTag() {
        return hbmTag;
    }

    public void setHbmTag(String hbmTag) {
        this.hbmTag = hbmTag;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

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
	
	
    @Override
    public String toString() {
        return "\n    Relationship ["
                + "referColumn=" + referColumn + ", referTable=" + referTable + ", referClass=" + referClass
                + ", type=" + type + "]";
    }
	
}
