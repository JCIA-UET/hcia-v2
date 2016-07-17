package uet.jcia.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;

@ManagedBean
@ViewScoped
public class TreeBean {
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	@ManagedProperty("#{treeService}")
	private TreeService treeService;
	
	@ManagedProperty("#{columnBean}")
	private ColumnBean columnBean;
	
	@ManagedProperty("#{tableBean}")
	private TableBean tableBean;
	
	public TableBean getTableBean() {
		return tableBean;
	}

	public void setTableBean(TableBean tableBean) {
		this.tableBean = tableBean;
	}

	public TreeService getTreeService() {
		return treeService;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	@PostConstruct
	public void init(){
		root = treeService.createTable();
	}
	
	public TreeNode getSelectedNode() {
        return selectedNode;
    }
	
	public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
	
	public void setTreeService(TreeService service){
		treeService = service;
	}
	
	public TreeNode getRoot() {
		return root;
	}
	public void onSelectNode(NodeSelectEvent event){
		if(event.getTreeNode().getData() instanceof Table){
			tableBean.setTable((Table)event.getTreeNode().getData());
			RequestContext.getCurrentInstance().update("dttable");
			RequestContext.getCurrentInstance().update("dttabler");
		    System.out.println(event.getTreeNode().getData());
		}
		if(event.getTreeNode().getData() instanceof Column){
			Column col = (Column)event.getTreeNode().getData();
			if(col.getLength().equals("")){
				col.setLength("DEFAULT");
			}
			columnBean.setColumn(col);
		    System.out.println(event.getTreeNode().getData());
			RequestContext.getCurrentInstance().update("dtcolumn");
		}
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}

	public void setColumnBean(ColumnBean columnBean) {
		this.columnBean = columnBean;
	}
}