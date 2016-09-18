package uet.jcia.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.RootNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;
import uet.jcia.utils.Constants;
import uet.jcia.utils.JsonHelper;

@ManagedBean
@SessionScoped
public class DownloadBean {
	private String sqlScript;
	private String rawData;
	
	// Getters
	public String getSqlScript() {
		return sqlScript;
	}
	public String getRawData() {
		return rawData;
	}
	
	// Setters
	public void setSqlScript(String script) {
		this.sqlScript = script;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	
	// Methods
	public void downloadXML(String szJson) {
	    String downloadPath = createDownloadPath(szJson);
	    makeDownloadAction(downloadPath);
	}
	
	public void downloadSQLScript(String szJson) {
	    String scriptPath = createSQLScriptPath(szJson);
	    makeDownloadAction(scriptPath);
	}
	
	private String createSQLScriptPath(String szJson) {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    HttpSession session = (HttpSession) ec.getSession(false);
	    
	    System.out.println("JSON: " + szJson);
	    String script = generateSQLScript(szJson);
	    
	    if(script != null) {
	    	String sessionid = session.getId();
	    	String scriptFilePath = Constants.TEMP_SOURCE_FOLDER + File.separator + "script-" + sessionid + ".txt";
	    	System.out.println("File: " + scriptFilePath);
	    	try {
	    		File scriptFile = new File(scriptFilePath);
				FileOutputStream fos = new FileOutputStream(scriptFile);
				
				// if file doesnt exists, then create it
				if (!scriptFile.exists()) {
					scriptFile.createNewFile();
				}
				
				// get the content in bytes
				byte[] contentInBytes = script.getBytes();
				
				fos.write(contentInBytes);
				fos.flush();
				fos.close();
				
				return scriptFilePath;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return null;	
	}
	
	private String createDownloadPath(String szJson) {
		System.out.println(szJson);
		if(szJson == null || szJson.equalsIgnoreCase("")) return null;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		HttpSession session = (HttpSession) exContext.getSession(false);
			
		RootNode root = JsonHelper.convertJsonToRootNode(szJson);
		updateRootNode(root);
		
		String sessionid = session.getId();
		String dataName = (String) session.getAttribute(sessionid + "data");
		String username = (String) session.getAttribute(sessionid + "username");
		System.out.println("Download bean: Username: " + username + ", dataname: " + dataName);
		String fullDataPath = Constants.TEMP_SOURCE_FOLDER + File.separator + dataName;
		System.out.println("Prepare for download: " + fullDataPath);
		
		return api.download(fullDataPath);
	}
	
	private String generateSQLScript(String szJson) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		
		String dataKey = session.getId() + "data";
		String dataName = (String) session.getAttribute(dataKey);
		System.out.println("Prepare for download: " + dataName);
		String fullDataPath = Constants.TEMP_SOURCE_FOLDER + File.separator + dataName;
		
		RootNode root = JsonHelper.convertJsonToRootNode(szJson);
		updateRootNode(root);
		
		try {
			String sql = api.generateCreationScript(fullDataPath);
			if (sql == null || sql.equals("")) {
				return null;
			}
			else return sql;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void makeDownloadAction(String filePath) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		
		if(filePath != null) {
			File file = new File(filePath);
			String fileName = file.getName();
			String contentType = ec.getMimeType(filePath);
			int contentLength = (int) file.length();
		    
		    ec.responseReset();
		    ec.setResponseContentType(contentType);
		    ec.setResponseContentLength(contentLength);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    
		    try {
				OutputStream os = ec.getResponseOutputStream();
				InputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[2048];
				int i = -1;
				while ((i = fis.read(buffer)) != -1) {
					os.write(buffer, 0, i);
				}
				
				os.flush();
				fis.close();
				os.close();
				
				fc.responseComplete();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	
	private void updateRootNode(RootNode root) {
		CoreAPI api = new CoreAPI();
		if(root != null)
			for(TreeNode table : root.getChilds()) api.updateData(table);
	}
	
}