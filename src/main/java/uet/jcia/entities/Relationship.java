/* File : Relationship.class 
 * Author : hieusonson9x@gmail.com
 * Details: to express one relationship between two table  
 * */

package uet.jcia.entities;

public class Relationship {
	private String joinColumn;
	private String referTable;
	private String type;
	
	
	public String getJoinColumn() {
		return joinColumn;
	}
	public void setJoinColumn(String joinColumn) {
		this.joinColumn = joinColumn;
	}
	public String getReferTable() {
		return referTable;
	}
	public void setReferTable(String referTable) {
		this.referTable = referTable;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String toString(){
		return "Relation: "+type+ ", join column: "+joinColumn +", refer table: "+referTable+"\n";
	}
	
}
