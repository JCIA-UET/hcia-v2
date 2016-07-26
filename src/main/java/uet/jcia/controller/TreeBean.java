package uet.jcia.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.TableNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6525378828188254901L;
	private TreeNode root;
	private String jsonTree;
	
	public TreeBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    String sessionid = session.getId();
	
	    String dirKey = sessionid + "dir";
	    String parsedFileDir = (String) session.getAttribute(dirKey);
	    
	    if(parsedFileDir != null) {
			TreeNode root = api.getParsedData(parsedFileDir);
			TreeNode copyroot = root;
			
			for(TreeNode t : copyroot.getChilds()) {
				TableNode table = (TableNode) t;
				List<TreeNode> colsList = table.getChilds();
				for(TreeNode c : colsList) {
					if(!(c instanceof ColumnNode)) {
						int index = colsList.indexOf(c);
						ColumnNode tempCol = new ColumnNode();
						colsList.set(index, tempCol);
					}
				}
			}
			
			System.out.println(copyroot);
			//Gson gson = new Gson();
			//String jsonTree = gson.toJson(root);
			//System.out.println(jsonTree);
			setRoot(root);
			//setJsonTree(jsonTree);
	    }
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public String getJsonTree() {
		return jsonTree;
	}

	public void setJsonTree(String jsonTree) {
		this.jsonTree = jsonTree;
	}
}