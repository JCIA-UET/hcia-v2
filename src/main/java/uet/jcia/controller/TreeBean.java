package uet.jcia.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.RootNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;
import uet.jcia.model.FileManager;
import uet.jcia.model.MapStorage;
import uet.jcia.utils.Constants;
import uet.jcia.utils.CookieHelper;
import uet.jcia.utils.Helper;
import uet.jcia.utils.JsonHelper;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6525378828188254901L;
	private String jsonData;
	

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonTree) {
		this.jsonData = jsonTree;
	}

	public TreeBean() {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext exContext = facesContext.getExternalContext();
//		CoreAPI api = new CoreAPI();
//
//		HttpSession session = (HttpSession) exContext.getSession(false);
//		
//		String dirKey = "parsedir";
//		String parsedDir = (String) session.getAttribute(dirKey);
//		
//		if(parsedDir != null) {
//			System.out.println("Current temp path: " + parsedDir);
//			TreeNode root = api.getParsedData(parsedDir);
//			String jsonData = JsonHelper.convertNodeToJson(root);
//				
//			setJsonData(jsonData);
//		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		
		String dataFileName = null;
		dataFileName = getDataFileNameFromCookie();
		
		System.out.println("Temp path: " + dataFileName);
		
		if (dataFileName != null) {
			String fullDataPath = Constants.TEMP_SOURCE_FOLDER + File.separator + dataFileName;

			HttpSession session = (HttpSession) exContext.getSession(false);
			String sessionid = session.getId();
			String parseDirKey = sessionid + "parsedir";
			
			setSessionProp(parseDirKey, fullDataPath);
			
			TreeNode root = api.getParsedData(fullDataPath);
			String jsonData = JsonHelper.convertNodeToJson(root);
				
			setJsonData(jsonData);
		}
	}

	public void updateTreeData(String json) {
		System.out.println("JSON: " + json);
		
		String crtTempPath = CookieHelper.getCookie("data").getValue();
		crtTempPath = Constants.TEMP_SOURCE_FOLDER + File.separator + crtTempPath;
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
				
				String tempKey = Helper.getFileName(tempPath);
				mapper.put(tempPath, value);
				
				setSessionProp("parsedir", tempPath);
				System.out.println("[Create new map] Key: " + tempPath + " | Value: " + value);
				
				CookieHelper.setCookie("data", tempKey);
				
				break;
			}
		}
		storage.saveMap(mapper);
	}
	
//	private void loadSavedSession(ComponentSystemEvent event) {
//		String dataFileName = null;
//		
//		dataFileName = getDataFileNameFromCookie();
//		System.out.println("Temp path: " + dataFileName);
//		
//		if (dataFileName != null) {
//			String fullDataPath = Constants.TEMP_SOURCE_FOLDER + File.separator + dataFileName;
//			String parseDirKey = "parsedir";
//			setSessionProp(parseDirKey, fullDataPath);
//		}
//	}
	
	private String getDataFileNameFromCookie() {
		Cookie[] userCookie = CookieHelper.getAllCookies();
		
		if (userCookie != null && userCookie.length > 0) {
			for (int i = 0; i < userCookie.length; i++) {
				if (userCookie[i].getName().equals("data")) {
					return userCookie[i].getValue();
				}
			}
		}
		
		return null;
	}
	
	private void setSessionProp(String key, String value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    exContext.getSessionMap().put(key, value);
	}
}