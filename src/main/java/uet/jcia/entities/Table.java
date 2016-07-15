/* File : Table.class 
 * Author : hieusonson9x@gmail.com
 * Details: to express a table in the DB  
 * */

package uet.jcia.entities;

import java.util.List;

public class Table {
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
	public String toString(){
		String result = "";
		result += "Table : " + tableName +"\n";
		for(Column col : listColumn){
			result+=col;
		}
		for(Relationship re:listRelationship){
			result+=re;
		}
		return result;
	}
}
