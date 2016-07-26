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
        List<Table> tableList;
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
            
            tableList = parser.parseXml(dstPath);
            resultPath = fm.saveTables(tableList);
            mapper.put(resultPath, dstDir);
            
        } else if (uploadPath.matches(".*\\.zip")) {
            String extractedFolder = zm.unzip(uploadPath);
            List<String> xmlList = new ArrayList<>();
            fm.findFiles(
                    extractedFolder, ".*\\.xml", xmlList);

            tableList = parser.parseXmlList(xmlList);
            resultPath = fm.saveTables(tableList);
            String documentPath = fm.saveDocumentsHash(parser.getCachedDocument());
            
            mapper.put(resultPath, extractedFolder);
            tempDocumentMapper.put(resultPath, documentPath);
            
        }
        
        return resultPath;
    }
    
    public List<Table> getTableList(String tempPath) {
        List<Table> list = fm.readTables(tempPath);
        return list;
    }
    
    public String download(String tempPath) {
        if (mapper.get(tempPath) == null) {
            return null;
        } else {
            HashMap<String, Document> tagMapper = 
                    fm.readDocumentsHash(tempDocumentMapper.get(tempPath));
            for (String xmlPath : tagMapper.keySet()) {
                Document doc = tagMapper.get(xmlPath);
                inverser.saveXml(xmlPath, doc);
            }
            
            return zm.compress(mapper.get(tempPath));
        }
    }
    
    public void updateData(List<Table> modifiedTables, String tempPath) {
        HashMap<String, Document> tagMapper = 
                fm.readDocumentsHash(tempDocumentMapper.get(tempPath));
        for (Table tbl : modifiedTables) {
            Document doc = tagMapper.get(tbl.getRefXml());
            inverser.updateTable(tbl, doc);
            fm.saveDocumentsHash(tagMapper);
        }
    }
    
}
