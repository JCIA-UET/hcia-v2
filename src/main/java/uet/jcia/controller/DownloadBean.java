package uet.jcia.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class DownloadBean {
	private String rawData;
	
	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	@SuppressWarnings("unchecked")
	public void prepareContent(String szJson) {
		System.out.println("Main data: " + rawData);
		System.out.println("Raw data: " + szJson);
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			List<TableNode> finalList = (List<TableNode>) mapper.readValue(rawData, TableNode.class);
//			System.out.println(finalList);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext exContext = facesContext.getExternalContext();
//		CoreAPI api = new CoreAPI();
//		HttpSession session = (HttpSession) exContext.getSession(false);
//
//		String sessionid = session.getId();
//		String resultDirKey = sessionid + "origindir";
//
//		String resultDir = (String) session.getAttribute(resultDirKey);
//		
//		System.out.println("Parsed dir:" + resultDir);
//		return api.download(resultDir);
	}

	public void download(String szJson) {
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();
	    
	    prepareContent(szJson);
//	    System.out.println("Download file: " + dlFile);
//		File file = new File(dlFile);
//		String fileName = file.getName();
//		String contentType = ec.getMimeType(dlFile);
//		int contentLength = (int) file.length();
//	    
//	    ec.responseReset();
//	    ec.setResponseContentType(contentType);
//	    ec.setResponseContentLength(contentLength);
//	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//	    
//	    try {
//			OutputStream os = ec.getResponseOutputStream();
//			InputStream fis = new FileInputStream(file);
//			byte[] buffer = new byte[2048];
//			int i = -1;
//			while ((i = fis.read(buffer)) != -1) {
//				os.write(buffer, 0, i);
//			}
//			
//			os.flush();
//			fis.close();
//			os.close();
//			
//			fc.responseComplete();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
