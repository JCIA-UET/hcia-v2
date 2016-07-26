package uet.jcia.entities;

import java.util.List;

import org.w3c.dom.Element;

public abstract class TreeNode {

    protected List<TreeNode> childs;
    protected TreeNode parent;
    protected Element linkedElement;

    protected long tempId;

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
