package uet.jcia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;

public class CoreAPI {
    
    private ZipManager zm = new ZipManager();
    private FileManager fm = new FileManager();
    private Parser parser = new Parser();
    private Inverser inverser = new Inverser();
    
    // mapping between temp file and extracted folder
    private static HashMap<String, String> mapper = new HashMap<>(); 
    
    /**
     * gửi path zip vào, nó sẽ gửi path temp lưu table
     * @param zipPath absolute path của file zip
     * @return trả path file temp
     */
    public String parse(String zipPath) {
        // extract
        String extractedFolder = zm.unzip(zipPath);
        // scan
        List<String> xmlList = new ArrayList<>();
        fm.findFiles(
                extractedFolder, ".*\\.xml", xmlList);
        // parse
        List<Table> tableList = parser.parseXmlList(xmlList);
        // save
        String resultPath = fm.saveTables(tableList);
        
        mapper.put(resultPath, extractedFolder);
        
        return resultPath;
    }
    
    /**
     * lấy danh sách table đã được lưu
     * @param tempPath nhận vào path file đã nhận từ lúc parse
     * @return danh sách table
     */
    public List<Table> getTableList(String tempPath) {
        return fm.readTables(tempPath);
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
        inverser.updateHbmClass(tbl, doc);
    }
    
    public void updateColumn(Column col) {
        Document doc = parser.getTagMapper().get(col.getRefXml());
        
        if (col.isPrimaryKey()) {
            inverser.updateHbmId(col, doc);
        } else {
            inverser.updateHbmProperty(col, doc);
        }
    }

}
