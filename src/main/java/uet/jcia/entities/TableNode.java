package uet.jcia.entities;

public class TableNode extends TreeNode {
    
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
