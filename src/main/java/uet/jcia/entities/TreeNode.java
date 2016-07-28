package uet.jcia.entities;

import java.io.Serializable;
import java.util.List;

import org.w3c.dom.Element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, 
        include = JsonTypeInfo.As.PROPERTY, 
        property = "json")
@JsonSubTypes({
    @JsonSubTypes.Type(value=TableNode.class, name="table"),
    @JsonSubTypes.Type(value=ColumnNode.class, name="column"),
    @JsonSubTypes.Type(value=RelationshipNode.class, name="relationship"),
    @JsonSubTypes.Type(value=RootNode.class, name="rootnode"),
    @JsonSubTypes.Type(value=PrimaryKeyNode.class, name="pk"),
    @JsonSubTypes.Type(value=MTORelationshipNode.class, name="mto"),
	@JsonSubTypes.Type(value=OTMRelationshipNode.class, name="otm")
})
public class TreeNode implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1318575025108283976L;
	protected List<TreeNode> childs;
	@JsonIgnore
    protected TreeNode parent;
	@JsonIgnore
    protected Element linkedElement;

    protected long tempId;
    
    protected String javaName;
    
    public String getJavaName() {
        return javaName;
    }
    
    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public long getTempId() {
        return tempId;
    }

    public void setTempId(long tempId) {
        this.tempId = tempId;
    }

    public List<TreeNode> getChilds() {
        return childs;
    }

    public void setChilds(List<TreeNode> childs) {
        this.childs = childs;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public Element getLinkedElement() {
        return linkedElement;
    }

    public void setLinkedElement(Element linkedElement) {
        this.linkedElement = linkedElement;
    }

    @Override
    public String toString() {
        return "TreeNode [" + "\n    linkedElement=" + linkedElement + "," + "\n    tempId=" + tempId + ","
                + "\n    childs=" + childs + ",";
    }

}
