package uet.jcia.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("table")
public class TableNode extends TreeNode {
	private static final long serialVersionUID = 5729963936298175858L;
	
	protected String className;
    protected String tableName;
    protected String catalog;
    
    protected String xmlPath;
    
    public TableNode(){
    	
    }
    
    public String getCatalog() {
        return catalog;
    }
    
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    
    public String getXmlPath() {
        return xmlPath;
    }
    
    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }
    
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
