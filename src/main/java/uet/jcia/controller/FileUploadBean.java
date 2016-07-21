//package uet.jcia.controller;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//
//import org.primefaces.event.FileUploadEvent;
//import org.primefaces.model.UploadedFile;
//
//import uet.jcia.entities.Table;
//import uet.jcia.model.Extractor;
//import uet.jcia.model.InteractComponent;
//import uet.jcia.model.Parser;
//import uet.jcia.model.Scanner;
//
//@ManagedBean
//public class FileUploadBean {
//	
//	@ManagedProperty("#{treeBean}")
//	private TreeBean treeBean;
//	
//	public void setTreeBean(TreeBean treeBean) {
//		this.treeBean = treeBean;
//	}
//	
//    public void handleFileUpload(FileUploadEvent event) {
//    	InteractComponent ic = InteractComponent.getIntance();
//    	Parser parser = new Parser();
//    	Extractor ext = new Extractor();
//    	Scanner scanner = new Scanner();
//    	UploadedFile uploadedFile = event.getFile();
//        String fileName = uploadedFile.getFileName();
//        List<String> filesList = new ArrayList<>();
//        
//        try {
//        	if(fileName.endsWith(".hbm.xml")) {
//				InputStream input = uploadedFile.getInputstream();
//				//ext.saveUploadFile(input, fileName);
//				String fileDir = scanner.getPath(fileName);
//				filesList.add(fileDir);
//				System.out.println(fileDir);
//				List<Table> tablesList = new ArrayList<>();
//				tablesList = parser.parseAllToListTable(filesList);
//				ic.setListTable(tablesList);
//        	}
//        	else if(fileName.endsWith(".zip")) {
//				InputStream input = uploadedFile.getInputstream();
//				ext.saveUploadFile(input, fileName);
//				String fileDir = scanner.getPath(fileName);
//				filesList.add(fileDir);
//				System.out.println(fileDir);
//				filesList = scanner.searchAndExtractXmlFile(fileName);
//				List<Table> tablesList = new ArrayList<>();
//				tablesList = parser.parseAllToListTable(filesList);
//				ic.setListTable(tablesList);
//        	}
//        	ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
//        	context.redirect("home.xhtml");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//}
