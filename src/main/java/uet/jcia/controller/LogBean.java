package uet.jcia.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class LogBean {
	private List<String> logs;
	
	public List<String> getLogs() {
		return logs;
	}

	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
	
	@SuppressWarnings("unchecked")
	public LogBean() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		String logKey = sessionid + "log";
		List<String> changeLog = (List<String>) session.getAttribute(logKey);
		System.out.println(changeLog);
		setLogs(changeLog);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> showLogs() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		String logKey = sessionid + "log";
		List<String> changeLog = (List<String>) session.getAttribute(logKey);
		System.out.println(changeLog);
		return changeLog;
	}
	
	public void restore(String fileName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		
		HttpSession session = (HttpSession) exContext.getSession(false);
		String sessionid = session.getId();
		
		String parsedFileKey = sessionid + "origindir";
		exContext.getSessionMap().put(parsedFileKey, fileName);
	}
}
