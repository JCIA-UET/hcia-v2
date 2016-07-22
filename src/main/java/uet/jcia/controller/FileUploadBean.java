package uet.jcia.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;
import uet.jcia.utils.Constants;

@ManagedBean
@SessionScoped
public class FileUploadBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Part file;

	private boolean validPass;

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
	    HttpSession session = (HttpSession) exContext.getSession(true);
		

		CoreAPI core = new CoreAPI();

		String fileName = getFileName(file);
		InputStream is;
		try {
			is = file.getInputStream();
			
			String fileDir = saveFile(is, fileName);
			System.out.println("Saved file's directory: " + fileDir);
			String parseResultDir = core.parse(fileDir);
			System.out.println("Parse file's directory: " + parseResultDir);
			
			if(parseResultDir != null) {
				String sessionid = session.getId();
				List<Table> list = core.getTableList(parseResultDir);
				String ssTableKey = sessionid + "table";
				
				exContext.getSessionMap().put(sessionid, parseResultDir);
				exContext.getSessionMap().put(ssTableKey, list);
			}
			
			exContext.redirect("demo2.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void validateZipFile(FacesContext ctx, UIComponent comp, Object value) {
		FacesMessage msg = null;
		Part file = (Part) value;
		this.validPass = false;

		String fileName = file.getSubmittedFileName();
		if (file.getSize() == 0) {
			msg = new FacesMessage("File is required to upload.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		} else if (!fileName.endsWith("zip")) {
			msg = new FacesMessage("Only allowed ZIP file.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}

		if (msg == null) {
			this.validPass = true;
		} else if (!(msg == null)) {
			FacesContext.getCurrentInstance().addMessage("input-file", msg);
			throw new ValidatorException(msg);
		}
	}

	public void validateXmlFile(FacesContext ctx, UIComponent comp, Object value) {
		FacesMessage msg = null;
		Part file = (Part) value;
		this.validPass = false;

		String fileName = file.getSubmittedFileName();
		if (file.getSize() == 0) {
			msg = new FacesMessage("File is required to upload.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		} else if (!fileName.endsWith("hbm.xml")) {
			msg = new FacesMessage("Only allowed XML file.");
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
