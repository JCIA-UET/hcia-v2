package uet.jcia.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("otm")
public class OTMRelationshipNode extends RelationshipNode {
	private static final long serialVersionUID = -4119402082533721551L;
	
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
             + "\n                 javaName=" + javaName + ","
             + "\n                 referTable=" + ((referTable == null) ? null : referTable.getTableName()) + "]";
    }
}
