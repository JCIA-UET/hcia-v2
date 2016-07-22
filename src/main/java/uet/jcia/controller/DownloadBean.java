package uet.jcia.controller;

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
	public void download() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
	    HttpSession session = (HttpSession) exContext.getSession(false);
	    
	    String sessionid = session.getId();
	    String ssTableKey = sessionid + "table";
	    
	    String resultDir = (String) session.getAttribute(sessionid);
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);
		
		CoreAPI api = new CoreAPI();
		for(Table t : list) {
			api.updateTable(t);
		}
		
		String downloadURL = api.download(resultDir);
	}
}
