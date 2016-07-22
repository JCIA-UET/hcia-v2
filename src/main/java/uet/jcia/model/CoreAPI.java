package uet.jcia.model;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

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
    
    /**
     * gửi path zip vào, nó sẽ gửi path temp lưu table
     * @param uploadPath absolute path của file zip
     * @return trả path file temp
     */
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
            mapper.put(resultPath, extractedFolder);
        }
        
        return resultPath;
    }
    
    /**
     * lấy danh sách table đã được lưu
     * @param tempPath nhận vào path file đã nhận từ lúc parse
     * @return danh sách table
     */
    public List<Table> getTableList(String tempPath) {
        List<Table> list = fm.readTables(tempPath);
        return list;
    }
    
    /**
     * download các file xml
     * @param tempPath nhận vào path file đã nhận từ lúc parse
     * @return path tới file đã được zip
     */
    public String download(String tempPath) {
        if (mapper.get(tempPath) == null) {
            return null;
        } else {
            HashMap<String, Document> tagMapper = parser.getTagMapper();
            for (String xmlPath : tagMapper.keySet()) {
                Document doc = tagMapper.get(xmlPath);
                inverser.saveXml(xmlPath, doc);
            }
            
            return zm.compress(mapper.get(tempPath));
        }
    }
    
    public void updateTable(Table tbl) {
        Document doc = parser.getTagMapper().get(tbl.getRefXml());
        inverser.updateTable(tbl, doc);
    }
    
    public void updateData(List<Table> modifiedTables) {
        for (Table tbl : modifiedTables) {
            Document doc = parser.getTagMapper().get(tbl.getRefXml());
            inverser.updateTable(tbl, doc);
        }
    }
    
    /*public void updateColumn(Column col) {
        Document doc = parser.getTagMapper().get(col.getRefXml());
        
        if (col.isPrimaryKey()) {
            inverser.updateHbmId(col, doc);
        } else {
            inverser.updateHbmProperty(col, doc);
        }
    }*/

}
