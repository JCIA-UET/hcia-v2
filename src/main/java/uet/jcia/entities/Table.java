/* File : Table.class 
 * Author : hieusonson9x@gmail.com
 * Details: to express a table in the DB  
 * */

package uet.jcia.entities;

import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {

    private static final long serialVersionUID = 9196607214146645073L;
    
    private String refXml;
    private String className;
	private String tableName;
	private List<Column> listColumn;
	private List<Relationship> listRelationship;
	
	public Table(){
		
	}
	
	public List<Relationship> getListRelationship() {
		return listRelationship;
	}
	public void setListRelationship(List<Relationship> listRelationship) {
		this.listRelationship = listRelationship;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	public List<Column> getListColumn() {
		return listColumn;
	}
	public void setListColumn(List<Column> listColumn) {
		this.listColumn = listColumn;
	}
	
	public String getClassName() {
        return className;
    }
	
	public void setClassName(String className) {
        this.className = className;
    }
	
	public String getRefXml() {
        return refXml;
    }
	
	public void setRefXml(String refXml) {
        this.refXml = refXml;
    }

    @Override
    public String toString() {
        return "\nTable [\n"
                + "  className=" + className + ", tableName=" + tableName + ", refXml = " + refXml + "\n"
                + "  listColumn=" + listColumn + ",\n"
                + "  listRelationship=" + listRelationship + "]";
    }
	
}
