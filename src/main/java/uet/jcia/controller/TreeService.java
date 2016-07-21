package uet.jcia.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    String sessionid = session.getId();
		
	    String resultDir = (String) session.getAttribute(sessionid);
	    
	    if(resultDir != null) {
			List<Table> list = core.getTableList(resultDir);
			//System.out.println("Table List:" + list);
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
	    return null;
	}
}