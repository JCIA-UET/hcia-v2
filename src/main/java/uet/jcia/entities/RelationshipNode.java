package uet.jcia.entities;

import java.io.Serializable;

public class RelationshipNode extends TreeNode implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3795945845604632682L;
	protected TableNode referTable;
    
    public TableNode getReferTable() {
        return referTable;
    }
   
    public void setReferTable(TableNode referTable) {
        this.referTable = referTable;
    }
    
}
