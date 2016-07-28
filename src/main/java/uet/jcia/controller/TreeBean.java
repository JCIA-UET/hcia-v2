package uet.jcia.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.TableNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;
import uet.jcia.utils.Helper;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {

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

		if (parsedFileDir != null) {
			TreeNode root = api.getParsedData(parsedFileDir);

			try {
				ObjectMapper mapper = new ObjectMapper();
				String jsonTree = mapper.writeValueAsString(root);

				System.out.println(jsonTree);
				
				String jsonKey = sessionid + "json";
				exContext.getSessionMap().put(jsonKey, jsonTree);
				
				setRoot(root);
				setJsonTree(jsonTree);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated
				e.printStackTrace();
			}

		}
	}
	
	public void save(String jsonData) {
		System.out.println(jsonData);
		/*try {
			ObjectMapper mapper = new ObjectMapper();
			TableNode changedTable = mapper.readValue(jsonData, TableNode.class);
			System.out.println(changedTable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
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