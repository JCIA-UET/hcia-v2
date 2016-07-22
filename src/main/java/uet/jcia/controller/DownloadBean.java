package uet.jcia.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class DownloadBean {
	@SuppressWarnings("unchecked")
	public String getDownloadDir() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) exContext.getSession(false);

		String sessionid = session.getId();
		String ssTableKey = sessionid + "table";

		String resultDir = (String) session.getAttribute(sessionid);
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);

		CoreAPI api = new CoreAPI();
		for (Table t : list) {
			api.updateTable(t);
		}

		String downloadDir = api.download(resultDir);
		return downloadDir;
	}

	public void download() {
		String downloadDir = getDownloadDir();
		File file = new File(downloadDir);
		try {
			InputStream fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int offset = 0;
			int numRead = 0;
			while ((offset < buf.length) && ((numRead = fis.read(buf, offset, buf.length - offset)) >= 0)) {
				offset += numRead;
			}
			fis.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
