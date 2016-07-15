package uet.jcia.entities;

import java.util.List;

public class Table {
	private String tableName;
	private List<Column> listColumn;
	
	
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
		
		return result;
	}
}
