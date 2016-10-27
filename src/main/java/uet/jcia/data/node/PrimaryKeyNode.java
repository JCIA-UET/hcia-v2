package uet.jcia.data.node;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("pk")
public class PrimaryKeyNode extends ColumnNode {
	private static final long serialVersionUID = 4385971629550030932L;
	
	private boolean autoIncrement;
    
    public boolean isAutoIncrement() {
        return autoIncrement;
    }
    
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
