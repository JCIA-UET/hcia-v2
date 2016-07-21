package uet.jcia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            return zm.compress(mapper.get(tempPath));
        }
    }
    
    public void updateTable(Table tbl) {
        inverser.updateHbmClass(tbl);
    }
    
    public void updateColumn(Column col) {
        if (col.isPrimaryKey()) {
            inverser.updateHbmId(col);
        } else {
            inverser.updateHbmProperty(col);
        }
    }

}
