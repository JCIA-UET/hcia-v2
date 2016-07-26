package uet.jcia.entities;

public class PrimaryKeyNode extends ColumnNode {
    
    private boolean autoIncrement;
    
    public boolean isAutoIncrement() {
        return autoIncrement;
    }
    
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
