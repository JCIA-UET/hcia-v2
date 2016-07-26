package uet.jcia.entities;

public class OTMRelationshipNode extends RelationshipNode {

    private ColumnNode foreignKey;
    
    public ColumnNode getForeignKey() {
        return foreignKey;
    }
    
    public void setForeignKey(ColumnNode foreignKey) {
        this.foreignKey = foreignKey;
    }
    
    @Override
    public String toString() {
        return "\n             OTMRelationshipNode ["
             + "\n                 tempId=" + tempId + ","
             + "\n                 foreignKey=" + foreignKey + ","
             + "\n                 referTable=" + ((referTable == null) ? null : referTable.getTableName()) + "]";
    }
}
