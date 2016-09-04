//package uet.jcia.controller;
//
//import java.io.File;
//
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ComponentSystemEvent;
//import javax.servlet.http.Cookie;
//
//import uet.jcia.utils.Constants;
//import uet.jcia.utils.CookieHelper;
//
//@ManagedBean(name="cookieBean")
//@SessionScoped
//public class CookieBean {
//	
//	public void loadSavedSession(ComponentSystemEvent event) {
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
//	
//	private void setSessionProp(String key, String value) {
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext exContext = facesContext.getExternalContext();
//	    exContext.getSessionMap().put(key, value);
//	}
//	
//	private String getDataFileNameFromCookie() {
//		Cookie[] userCookie = CookieHelper.getAllCookies();
//		
//		if (userCookie != null && userCookie.length > 0) {
//			for (int i = 0; i < userCookie.length; i++) {
//				if (userCookie[i].getName().equals("data")) {
//					return userCookie[i].getValue();
//				}
//			}
//		}
//		
//		return null;
//	}
//}
