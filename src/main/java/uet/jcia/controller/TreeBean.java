package uet.jcia.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.TableNode;
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
		String sessionid = session.getId();

		//String jsonKey = sessionid + "json";
		//String jsonData = (String) session.getAttribute(jsonKey);
		
		String dirKey = sessionid + "origindir";
		String parsedDir = (String) session.getAttribute(dirKey);
		
		if(parsedDir != null) {
			try {
				TreeNode root = api.getParsedData(parsedDir);
				//System.out.println("Root: " + root);
				ObjectMapper mapper = new ObjectMapper();
				String jsonData = mapper.writeValueAsString(root);
				//System.out.println(tree);
				
				setRoot(root);
				setJsonData(jsonData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void save(String jsonData) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		String dirKey = sessionid + "origindir";
		String parsedDir = (String) session.getAttribute(dirKey);
		
		if(jsonData != null && parsedDir != null) {
			try {
				System.out.println(jsonData);
				ObjectMapper mapper = new ObjectMapper();
				TableNode changedTable = mapper.readValue(jsonData, TableNode.class);	
				api.updateData(changedTable);
				String newDir = api.refresh(parsedDir);
				File tempFile = new File(newDir);
				System.out.println("New path: " + newDir);
				exContext.getSessionMap().put(dirKey, newDir);
				
				String logKey = sessionid + "log";
				List<String> changeLog = (List<String>) session.getAttribute(logKey);
				
				changeLog.add(tempFile.getName());
				exContext.getSessionMap().put(logKey, changeLog);
				
				exContext.redirect("home.xhtml");
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