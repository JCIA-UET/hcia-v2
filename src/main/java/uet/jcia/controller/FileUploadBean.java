package uet.jcia.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import uet.jcia.dao.Account;
import uet.jcia.dao.AccountManager;
import uet.jcia.model.CoreAPI;
import uet.jcia.utils.Constants;
import uet.jcia.utils.CookieHelper;
import uet.jcia.utils.Helper;

@ManagedBean(name="fileUploadBean")
@SessionScoped
public class FileUploadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Part file;
	private boolean validPass;
	private AccountManager ac = new AccountManager();

	public boolean isValidPass() {
		return validPass;
	}

	public void setValidPass(boolean validPass) {
		this.validPass = validPass;
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public void upload() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext exContext = facesContext.getExternalContext();

		CoreAPI core = new CoreAPI();

		String fileName = getFileName(file);
		InputStream is;
		try {
			is = file.getInputStream();
			
			String fileDir = saveFile(is, fileName);
			System.out.println("Saved file's directory: " + fileDir);
			String tempDataPath = null;
			
			try {
				tempDataPath = core.parse(fileDir);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(tempDataPath != null) {
				HttpSession session = (HttpSession) exContext.getSession(false);
				String sessionid = session.getId();
				String dataName = Helper.getFileName(tempDataPath);
				
				String parseDirKey = sessionid + "data";
				exContext.getSessionMap().put(parseDirKey,dataName);
				
				String username = (String) session.getAttribute(sessionid + "username");
				
				Account acc = ac.getAccountByUsername(username);
				ac.setDataToAccount(dataName, acc);
			}
			
			exContext.redirect("index.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
		FacesMessage msg = null;
		Part file = (Part) value;
		this.validPass = false;

		String fileName = file.getSubmittedFileName();
		if (file.getSize() == 0) {
			msg = new FacesMessage("File is required to upload.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		} else if (!fileName.endsWith("zip") && !fileName.endsWith("hbm.xml")) {
			msg = new FacesMessage("Allowed extensions: zip, hbm.xml");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}

		if (msg == null) {
			this.validPass = true;
		} else if (!(msg == null)) {
			FacesContext.getCurrentInstance().addMessage("input-file", msg);
			throw new ValidatorException(msg);
		}
	}

	private static String getFileName(Part part) {
		return part.getSubmittedFileName();
	}

	private String saveFile(InputStream is, String fileName) {
		byte[] buffer = new byte[1024000];
		String fileDir = null;
		try {
			fileDir = Constants.TEMP_SOURCE_FOLDER + File.separator + fileName;
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(fileDir)));
			int i = -1;
			while ((i = is.read(buffer)) != -1) {
				bof.write(buffer, 0, i);
			}
			bof.close();
			return fileDir;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
