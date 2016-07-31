package uet.jcia.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("mto")
public class MTORelationshipNode extends RelationshipNode {
	private static final long serialVersionUID = -639214173809042259L;
	
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
		return "\n             MTORelationshipNode [" + "\n                 tempId=" + tempId + ","
				+ "\n                 foreignKey=" + foreignKey + "," + "\n                 referColumn="
				+ ((referColumn == null) ? null : referColumn.getColumnName()) + "," + "\n                 referTable="
				+ ((referTable == null) ? null : referTable.getTableName()) + "]";
	}

}
