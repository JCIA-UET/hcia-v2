package uet.jcia.model;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.entities.TreeNode;
import uet.jcia.utils.Constants;
import uet.jcia.utils.Helper;
import uet.jcia.utils.HibernateHelper;

public class CoreAPI {
    
    private ZipManager zm = new ZipManager();
    private FileManager fm = new FileManager();
    private Parser parser = new Parser();
    private JavaParser jParser = new JavaParser();
    private Inverser inverser = new Inverser();
    
    
    // mapping between temp file and extracted folder
    private static HashMap<String, String> mapper = new HashMap<>(); 
    
    // mapping between temp file and document file
    private static HashMap<String, String> tempDocumentMapper = new HashMap<>();
    
    public String parse(String uploadPath) throws Exception {
        TreeNode rootNode = null;
        String resultPath = null;

        // user uploads xml or java file
        if (uploadPath.matches(".*\\.xml") || uploadPath.matches(".*\\.java")) {
            Date date = new Date();
            String dstDir =  Constants.TEMP_SOURCE_FOLDER + File.separator
                    + "upload-src-" + Helper.DATE_FORMATER.format(date);
            File uploadedFile = new File(uploadPath);
            String dstPath = dstDir + File.separator + uploadedFile.getName();
            // copy file to generated folder
            Helper.copyFile(uploadPath, dstPath);

            if (uploadPath.matches(".*\\.xml")) {
            	rootNode = parser.parseXml(dstPath);
            } else if (uploadPath.matches(".*\\.java")) {
            	rootNode = jParser.parseJavaFile(dstPath);
            }
            
            resultPath = fm.saveTempData(rootNode);
            mapper.put(resultPath, dstDir);
            
        } else if (uploadPath.matches(".*\\.zip")) {
            String extractedFolder = zm.unzip(uploadPath);
            List<String> xmlList = new ArrayList<>();
            List<String> javaList = new ArrayList<>();
            fm.findFiles(extractedFolder, ".*\\.xml", xmlList);
            fm.findFiles(extractedFolder, ".*\\.java", javaList);
            
            if (!xmlList.isEmpty() && !javaList.isEmpty()) {
            	throw new Exception("can not parse both java and xml at the same time");
            } else if (!xmlList.isEmpty()) {
            	rootNode = parser.parseXmlList(xmlList);
            } else if (!javaList.isEmpty()) {
            	rootNode = jParser.parseJavaList(javaList);
            }
            
            resultPath = fm.saveTempData(rootNode);
            String documentPath = fm.saveDocumentsHash(parser.getCachedDocument());
            
            mapper.put(resultPath, extractedFolder);
            tempDocumentMapper.put(resultPath, documentPath);
            
        }
        
        return resultPath;
    }
    
    public String refresh(String tempPath) {
        String sourceFolder = mapper.get(tempPath);
        
        TreeNode rootNode;
        String resultPath = null;
        
        List<String> xmlList = new ArrayList<>();
        fm.findFiles(
                sourceFolder, ".*\\.xml", xmlList);

        rootNode = parser.parseXmlList(xmlList);
        resultPath = fm.saveTempData(rootNode);
        String documentPath = fm.saveDocumentsHash(parser.getCachedDocument());
        
        mapper.put(resultPath, sourceFolder);
        tempDocumentMapper.put(resultPath, documentPath);
        
        return resultPath;
    }
    
    public TreeNode getParsedData(String tempPath) {
        TreeNode rootNode = fm.readTempData(tempPath);
        return rootNode;
    }
    
    public String download(String tempPath) {
        return zm.compress(mapper.get(tempPath));
    }
    
    public String generateCreationScript(String tempPath) {
    	String extractedFolder = mapper.get(tempPath);
    	List<String> hbmList = new ArrayList<>();
    	fm.findFiles(extractedFolder, ".*\\.xml", hbmList);
    	return HibernateHelper.generateDdl(hbmList);
    }
    
    public void updateData(TreeNode tableNode) {
        inverser.updateTable(tableNode);
    }
    
}
