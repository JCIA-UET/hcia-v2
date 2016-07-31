package uet.jcia.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import uet.jcia.entities.TreeNode;
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
			String parsedResultDir = core.parse(fileDir);
			System.out.println("Parsed original file's directory: " + parsedResultDir);
			
			if(parsedResultDir != null) {
				String sessionid = session.getId();
				TreeNode root = core.getParsedData(parsedResultDir);
				
				//String sessionDir = Constants.TEMP_SOURCE_FOLDER + "\\" + sessionid + "\\" + sessionid;
				//Helper.copyFile(parsedResultDir, sessionDir);
				//System.out.println(parsedResultDir);
				
//				try {
//					ObjectMapper mapper = new ObjectMapper();
//					String jsonData = mapper.writeValueAsString(root);
//					
//					String jsonKey = sessionid + "json";
//					exContext.getSessionMap().put(jsonKey, jsonData);
//					
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated
//					e.printStackTrace();
//				}

				String logKey = sessionid + "log";
				List<String> changeLog = new ArrayList<>();
				File tempFile = new File(parsedResultDir);
				changeLog.add(tempFile.getName());
				System.out.println("First: " + changeLog);
				exContext.getSessionMap().put(logKey, changeLog);
				
				String originDirKey = sessionid + "origindir";
				exContext.getSessionMap().put(originDirKey, parsedResultDir);
				
				
			}
			
			exContext.redirect("index.xhtml");
		} catch (IOException e) {
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
