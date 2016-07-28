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

public class CoreAPI {
    
    private ZipManager zm = new ZipManager();
    private FileManager fm = new FileManager();
    private Parser parser = new Parser();
    private Inverser inverser = new Inverser();
    
    
    // mapping between temp file and extracted folder
    private static HashMap<String, String> mapper = new HashMap<>(); 
    
    // mapping between temp file and document file
    private static HashMap<String, String> tempDocumentMapper = new HashMap<>();
    
    public String parse(String uploadPath) {
        TreeNode rootNode;
        String resultPath = null;
        
        // user upload xml file
        if (uploadPath.matches(".*\\.xml")) {
            Date date = new Date();
            String dstDir =  Constants.TEMP_SOURCE_FOLDER + File.separator
                    + "upload-src-" + Helper.DATE_FORMATER.format(date);
            File xmlFile = new File(uploadPath);
            String dstPath = dstDir + File.separator + xmlFile.getName();
            // copy file to generated folder
            Helper.copyFile(uploadPath, dstPath);
            
            rootNode = parser.parseXml(dstPath);
            resultPath = fm.saveTempData(rootNode);
            mapper.put(resultPath, dstDir);
            
        } else if (uploadPath.matches(".*\\.zip")) {
            String extractedFolder = zm.unzip(uploadPath);
            List<String> xmlList = new ArrayList<>();
            fm.findFiles(
                    extractedFolder, ".*\\.xml", xmlList);

            rootNode = parser.parseXmlList(xmlList);
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
    
    public void updateData(TreeNode tableNode) {
        inverser.updateTable(tableNode);
    }
    
}
