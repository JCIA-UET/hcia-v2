package uet.jcia.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.entities.RootNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class DownloadBean {
	private String rawData;
	
	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	@SuppressWarnings("unchecked")
	public String createDownloadURL(String szJson) {
		if(szJson == null || szJson.equalsIgnoreCase("")) return null;
		System.out.println(szJson);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		HttpSession session = (HttpSession) exContext.getSession(false);
		
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			RootNode root = mapper.readValue(szJson, RootNode.class);
			
			if(root != null) {
				for(TreeNode table : root.getChilds()) {
					api.updateData(table);
				}
				
				String sessionid = session.getId();
				String resultDirKey = sessionid + "origindir";
				String resultDir = (String) session.getAttribute(resultDirKey);
				return api.download(resultDir);
			}
			return null;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void download(String szJson) {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    String downloadURL = createDownloadURL(szJson);
	    
	    if(downloadURL != null) {
	    
		    System.out.println("Download file: " + downloadURL);
			File file = new File(downloadURL);
			String fileName = file.getName();
			String contentType = ec.getMimeType(downloadURL);
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
}
