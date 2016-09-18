package uet.jcia.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.RootNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.dao.Account;
import uet.jcia.dao.AccountManager;
import uet.jcia.model.CoreAPI;
import uet.jcia.model.FileManager;
import uet.jcia.model.MapStorage;
import uet.jcia.utils.Constants;
import uet.jcia.utils.Helper;
import uet.jcia.utils.JsonHelper;

@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6525378828188254901L;
	private String jsonData;
	private String username;
	private AccountManager ac = new AccountManager();
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonTree) {
		this.jsonData = jsonTree;
	}

	public TreeBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();	

		String username = (String) session.getAttribute(sessionid + "username");
		
		Account acc = ac.getAccountByUsername(username);
		setUsername(acc.getUsername());
	}
	
	public String renderJson() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		CoreAPI api = new CoreAPI();
		
		String dataFileName = (String) session.getAttribute(sessionid + "data");
		
		Account acc = ac.getAccountByUsername(username);
		if (dataFileName == null) {
			dataFileName = ac.getAccountDataById(acc.getId());
			exContext.getSessionMap().put(sessionid + "data", dataFileName);
		}

		String fullDataPath = Constants.TEMP_SOURCE_FOLDER + File.separator + dataFileName;
		
		TreeNode root = api.getParsedData(fullDataPath);
		String jsonData = JsonHelper.convertNodeToJson(root);
			
		setJsonData(jsonData);
		return jsonData;
	}
	public void updateTreeData(String json) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		String username = (String) session.getAttribute(sessionid + "username");
		Account acc = ac.getAccountByUsername(username);
		String crtDataName = ac.getAccountDataById(acc.getId());
		
		String crtTempPath = Constants.TEMP_SOURCE_FOLDER + File.separator + crtDataName;
		System.out.println("Current temp path: " + crtTempPath);
		RootNode root = JsonHelper.convertJsonToRootNode(json);
		
		MapStorage storage = new MapStorage();
		HashMap<String, String> mapper = storage.loadMap();
		
		for(Entry<String, String> e : mapper.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();
			System.out.println("[Map] Key: " + key + " | Value: " + value);
			if (key.equals(crtTempPath)) {
				System.out.println("[Found map] Key: " + key + " | Value: " + value);
				
				FileManager fm = new FileManager();
				String tempPath = fm.saveTempData(root);
				System.out.println("Create new tempPath: " + tempPath);
				
				String simpleDataName = Helper.getFileName(tempPath);
				mapper.put(tempPath, value);
				
				setSessionProp(sessionid + "data", simpleDataName);
				System.out.println("[Create new map] Key: " + tempPath + " | Value: " + value);
				
				ac.setDataToAccount(simpleDataName, acc);
				break;
			}
		}
		storage.saveMap(mapper);
	}
	
	private void setSessionProp(String key, String value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    exContext.getSessionMap().put(key, value);
	}
}