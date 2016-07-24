package uet.jcia.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;

@ManagedBean(name = "modifyBean")
@SessionScoped
public class ModifyBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void resetdata() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) exContext.getSession(false);
		CoreAPI core = new CoreAPI();

		String sessionid = session.getId();
		String ssTableKey = sessionid + "table";

		String resultDir = (String) session.getAttribute(sessionid);
		List<Table> list = (List<Table>) session.getAttribute(ssTableKey);

		try {
			if (resultDir == null || list == null) {
				exContext.redirect("home.xhtml");

			} else {
				System.out.println("Not Null");
				list = core.getTableList(resultDir);
				System.out.println(list);
				session.setAttribute(ssTableKey, list);
				exContext.redirect("home.xhtml");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
