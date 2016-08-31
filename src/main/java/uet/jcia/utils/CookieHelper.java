package uet.jcia.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {
	
	public static void setCookie(String name, String value) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		
		Cookie cookie = getCookie(name);
		
		if (cookie != null) {
			cookie.setValue(value);
		}
		else {
			cookie = new Cookie(name, value);
			cookie.setPath(request.getContextPath());
		}
		
		cookie.setMaxAge(60*60*24*365*10);
		
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	    response.addCookie(cookie);
	}
	
	public static Cookie getCookie(String name) {
		Cookie cookie = null;
		Cookie[] userCookie = getAllCookies();
		
		if (userCookie != null && userCookie.length > 0) {
			for (int i = 0; i < userCookie.length; i++) {
				if(userCookie[i].getName().equalsIgnoreCase(name)) {
					cookie = userCookie[i];
					break;
				}
			}
		}
		return cookie;
	}
	
	public static Cookie[] getAllCookies() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
		
		Cookie[] userCookie = request.getCookies();
		
		if (userCookie != null && userCookie.length > 0) {
			for (int i = 0; i < userCookie.length; i++) {
				System.out.println("Cookie name: " + userCookie[i].getName() + " | Value: " + userCookie[i].getValue());
			}
		}
		
		return userCookie;
	}
}
