package uet.jcia.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("column")

public class ColumnNode extends TreeNode {
	private static final long serialVersionUID = 3175171924945900577L;
	
	protected String columnName;
    protected String dataType;
    protected int length;
    
    protected boolean primaryKey;
    protected boolean notNull;
    protected boolean foreignKey;
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public int getLength() {
        return length;
    }
    
    public void setLength(int length) {
        this.length = length;
    }
    
    public boolean isPrimaryKey() {
        return primaryKey;
    }
    
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    public boolean isNotNull() {
        return notNull;
    }
    
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
    
    public boolean isForeignKey() {
        return foreignKey;
    }
    
    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    @Override
    public String toString() {
        return "\n             ColumnNode ["
             + "\n                 tempId=" + tempId + ","
             + "\n                 columnName=" + columnName + ","
             + "\n                 dataType=" + dataType + ","
             + "\n                 length=" + length + ","
             + "\n                 primaryKey=" + primaryKey + ","
             + "\n                 notNull=" + notNull + ","
             + "\n                 foreignKey=" + foreignKey + "]";
    }
}
