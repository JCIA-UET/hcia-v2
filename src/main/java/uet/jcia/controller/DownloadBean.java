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

import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class DownloadBean {
	@SuppressWarnings("unchecked")
	public String getDownloadFile() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		CoreAPI api = new CoreAPI();
		HttpSession session = (HttpSession) exContext.getSession(false);

		String sessionid = session.getId();
		String resultDirKey = sessionid + "origindir";

		String resultDir = (String) session.getAttribute(resultDirKey);
		
		System.out.println("Parsed dir:" + resultDir);
		return api.download(resultDir);
	}

	public void download() {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    
	    String dlFile = getDownloadFile();
	    System.out.println("Download file: " + dlFile);
		File file = new File(dlFile);
		String fileName = file.getName();
		String contentType = ec.getMimeType(dlFile);
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
