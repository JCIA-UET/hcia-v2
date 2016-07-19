package uet.jcia.entities;

import java.io.Serializable;

public class Column implements Serializable {
    
    private static final long serialVersionUID = -4810176750275854793L;
    
    private String name;
	private String type;
	private boolean primaryKey;
	private boolean autoIncrement;
	private boolean notNull;
	private String length;
	
	private String hbmTag;
	private String mappingName;
	
	public Column(){

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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHbmTag() {
        return hbmTag;
    }

    public void setHbmTag(String hbmTag) {
        this.hbmTag = hbmTag;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    @Override
    public String toString() {
        return "\n    Column ["
                + "name=" + name + ", type=" + type + ", primaryKey=" + primaryKey + ", autoIncrement=" + autoIncrement
                + ",\n           notNull=" + notNull + ", length=" + length + ", hbmTag=" + hbmTag + ", mappingName=" + mappingName + "]";
    }

}