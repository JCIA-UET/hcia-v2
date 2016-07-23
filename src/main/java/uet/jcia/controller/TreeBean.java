package uet.jcia.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Table;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private List<Table> list;
	
	@SuppressWarnings("unchecked")
	public TreeBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    String sessionid = session.getId();
	
	    String ssTableKey = sessionid + "table";
	   
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);
		setList(list);
	}

	public List<Table> getList() {
		return list;
	}

	public void setList(List<Table> list) {
		this.list = list;
	}
	
	
	/*private TreeNode root;
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
	}*/
}