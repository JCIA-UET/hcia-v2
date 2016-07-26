package uet.jcia.entities;

import java.io.Serializable;

public class TableNode extends TreeNode  implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5729963936298175858L;
	protected String className;
    protected String tableName;
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "\n        TableNode ["
                + "\n            tempId=" + tempId + ","
                + "\n            className=" + className + ","
                + "\n            tableName=" + tableName + ","
                + "\n            childs=" + childs + "]";
    }
    
}
