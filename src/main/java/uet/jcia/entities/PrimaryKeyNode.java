package uet.jcia.entities;

import java.io.Serializable;

public class PrimaryKeyNode extends ColumnNode implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4385971629550030932L;
	private boolean autoIncrement;
    
    public boolean isAutoIncrement() {
        return autoIncrement;
    }
    
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
