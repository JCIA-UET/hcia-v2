package uet.jcia.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6525378828188254901L;
	private TreeNode root;
	private String jsonData;

	public TreeBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();

		HttpSession session = (HttpSession) exContext.getSession(false);
		
		String dirKey = "parsedir";
		String parsedDir = (String) session.getAttribute(dirKey);
		
		if(parsedDir != null) {
			try {
				TreeNode root = api.getParsedData(parsedDir);
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(root);
				
				setRoot(root);
				setJsonData(jsonData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonTree) {
		this.jsonData = jsonTree;
	}
}