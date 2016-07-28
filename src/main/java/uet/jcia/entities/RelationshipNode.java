package uet.jcia.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("relationship")
public class RelationshipNode extends TreeNode {
	private static final long serialVersionUID = 3795945845604632682L;
	
	protected TableNode referTable;
	protected String type;
	
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TableNode getReferTable() {
        return referTable;
    }
   
    public void setReferTable(TableNode referTable) {
        this.referTable = referTable;
    }
    
}
