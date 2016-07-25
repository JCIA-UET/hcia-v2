package cuong;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;

public class TestCoreAPI {

    static CoreAPI api = new CoreAPI();
    
    public static void main(String[] args) {
//        testZip();
        testXml();
        
    }
    
    public static void testZip() {
        String resultPath = api.parse("C:\\Users\\dinht_000\\workspace\\hcia-v2\\temp\\vnu.zip");
        System.out.println("[temp data] " + resultPath);
        
        List<Table> tableList = api.getTableList(resultPath);
        
        System.out.println(tableList);
        
        Table tbl = new Table();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\tModify table");
        System.out.println("RefXml: ");
        tbl.setRefXml(scanner.nextLine());
        System.out.println("TempId: ");
        tbl.setTempId(scanner.nextLine());
        System.out.println("Table name: ");
        tbl.setTableName(scanner.nextLine());
        scanner.close();
        
        api.updateTable(tbl);
        
        System.out.println(api.download(resultPath));
    }

    public static void testXml() {
        String resultPath = api.parse("C:\\Users\\dinht_000\\workspace\\hcia-v2\\temp\\Address.hbm.xml");
        System.out.println("[temp data] " + resultPath);
        
        List<Table> tableList = api.getTableList(resultPath);
        
        Table modifiedTable = tableList.get(0);
        Column modifiedCol = modifiedTable.getListColumn().get(2);
        
        System.out.println(tableList);
        
        // update data 
        Table tbl = new Table();
        
        // sá»­a thÃ´ng tin table
        System.out.println("\n\n\n\tModify table");
        // cáº§n láº¥y Ä‘Æ°á»£c refXml cá»§a table
        System.out.println("RefXml: " + modifiedTable.getRefXml());
        tbl.setRefXml(modifiedTable.getRefXml());
        // cÅ©ng cáº§n láº¥y Ä‘c temp_id cá»§a nÃ³
        System.out.println("TempId: " + modifiedTable.getTempId());
        tbl.setTempId(modifiedTable.getTempId());
        
        // sá»­a tÃªn table thÃ nh NEW_ORDERITEM
        //System.out.println("New table name: NEW_ORDERITEM");
        //tbl.setTableName("NEW_ORDERITEM");
        
        // sá»­a column
        Column col = new Column();
        System.out.println("\tModify column");
        // cáº§n láº¥y Ä‘Æ°á»£c temp_id cá»§a column
        System.out.println("TempId: " + modifiedCol.getTempId());
        col.setTempId(modifiedCol.getTempId());
        
        // sá»­a tÃªn
        System.out.println("New name: NEW_NAME");
        col.setName("NEW_NAME");
        
        // sá»­a kiá»ƒu
        System.out.println("New type: VARCHAR");
        col.setType("VARCHAR");
        
        // sá»­a Ä‘á»™ dÃ i
        System.out.println("New length: 15");
        col.setLength("15");
        
        // táº¡o List<Column> rá»“i add vÃ o table
        List<Column> colList = new ArrayList<>();
        colList.add(col);
        tbl.setListColumn(colList);
        
        // táº¡o List<Table> Ä‘á»ƒ truyá»�n cho CoreAPI.updateData()
        List<Table> tblList = new ArrayList<>();
        tblList.add(tbl);
        
        // gá»�i hÃ m updateData()
        api.updateData(tblList);
        
        System.out.println(api.download(resultPath));
    }
}

