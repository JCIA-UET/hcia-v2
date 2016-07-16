package uet.jcia.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.TreeNode;

@ManagedBean
public class TreeBean {
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	@ManagedProperty("#{treeService}")
	private TreeService treeService;
	
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
}