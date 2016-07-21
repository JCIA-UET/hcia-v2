package uet.jcia.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;


@ManagedBean(name = "treeService")
@SessionScoped
public class TreeService {
	public TreeNode createTable(){
		CoreAPI core = new CoreAPI();
		String resultLink = core.parse("C:\\Users\\vy\\workspace\\hcia-v2\\temp\\upload\\vnu.zip"); 
		FacesContext context2 = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) context2.getExternalContext().getSession(true);
	    session.setAttribute("resultLink", resultLink );
		
		List<Table> list = core.getTableList(resultLink);
		
		TreeNode root = new DefaultTreeNode("root",null) ;
		
		if(list == null) {
			return root;
		}
		else {
			for(Table table : list){
				TreeNode tableNode = new DefaultTreeNode("table",table,root);
				List<Column> listColumn = table.getListColumn();
				for(Column column:listColumn){
					tableNode.getChildren().add(new DefaultTreeNode("column",column,tableNode));
				}
			}
		}
		
		return root;
	}
}