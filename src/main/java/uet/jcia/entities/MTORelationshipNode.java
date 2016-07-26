package uet.jcia.entities;

public class MTORelationshipNode extends RelationshipNode {
    
    private ColumnNode foreignKey;
    private PrimaryKeyNode referColumn;
    
    public ColumnNode getForeignKey() {
        return foreignKey;
    }
    
    public void setForeignKey(ColumnNode foreignKey) {
        this.foreignKey = foreignKey;
    }
    
    public PrimaryKeyNode getReferColumn() {
        return referColumn;
    }
    
    public void setReferColumn(PrimaryKeyNode referColumn) {
        this.referColumn = referColumn;
    }

    @Override
    public String toString() {
        return "\n             MTORelationshipNode ["
             + "\n                 tempId=" + tempId + ","
             + "\n                 foreignKey=" + foreignKey + ","
             + "\n                 referColumn=" + ((referColumn == null) ? null : referColumn.getColumnName()) + ","
             + "\n                 referTable=" + ((referTable == null) ? null : referTable.getTableName()) + "]";
    }
    
}
