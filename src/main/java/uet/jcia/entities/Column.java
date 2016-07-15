package uet.jcia.entities;

public class Column {
	private String name;
	private String type;
	private boolean primaryKey;
	private boolean autoIcrement;
	private boolean not_null;
	
	
	public boolean isNot_null() {
		return not_null;
	}
	public void setNot_null(boolean not_null) {
		this.not_null = not_null;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isAutoIcrement() {
		return autoIcrement;
	}
	public void setAutoIcrement(boolean autoIcrement) {
		this.autoIcrement = autoIcrement;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString(){
		
		return "Column: "+  name+ "(" +type+ ","+ "AI:" + autoIcrement +", " + "Primary key:"+primaryKey+", Not-null:"+not_null+")"+"\n" ;
	}
}
