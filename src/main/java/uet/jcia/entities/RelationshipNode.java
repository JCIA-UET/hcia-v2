package uet.jcia.entities;

public class RelationshipNode extends TreeNode {

    protected TableNode referTable;
    
    public TableNode getReferTable() {
        return referTable;
    }
   
    public void setReferTable(TableNode referTable) {
        this.referTable = referTable;
    }
    
}
