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
        String resultPath = api.parse("I:\\Workspace\\hcia-v2\\temp\\upload\\vnu.zip");
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
        String resultPath = api.parse("I:\\Workspace\\hcia-v2\\temp\\upload\\OrderItem.hbm.xml");
        System.out.println("[temp data] " + resultPath);
        
        List<Table> tableList = api.getTableList(resultPath);
        
        Table modifiedTable = tableList.get(0);
        Column modifiedCol = modifiedTable.getListColumn().get(2);
        
        System.out.println(tableList);
        
        // update data 
        Table tbl = new Table();
        
        // sửa thông tin table
        System.out.println("\n\n\n\tModify table");
        // cần lấy được refXml của table
        System.out.println("RefXml: " + modifiedTable.getRefXml());
        tbl.setRefXml(modifiedTable.getRefXml());
        // cũng cần lấy đc temp_id của nó
        System.out.println("TempId: " + modifiedTable.getTempId());
        tbl.setTempId(modifiedTable.getTempId());
        
        // sửa tên table thành NEW_ORDERITEM
        System.out.println("New table name: NEW_ORDERITEM");
        tbl.setTableName("NEW_ORDERITEM");
        
        // sửa column
        Column col = new Column();
        System.out.println("\tModify column");
        // cần lấy được temp_id của column
        System.out.println("TempId: " + modifiedCol.getTempId());
        col.setTempId(modifiedCol.getTempId());
        
        // sửa tên
        System.out.println("New name: NEW_NAME");
        col.setName("NEW_NAME");
        
        // sửa kiểu
        System.out.println("New type: VARCHAR");
        col.setType("VARCHAR");
        
        // sửa độ dài
        System.out.println("New length: 15");
        col.setLength("15");
        
        // tạo List<Column> rồi add vào table
        List<Column> colList = new ArrayList<>();
        colList.add(col);
        tbl.setListColumn(colList);
        
        // tạo List<Table> để truyền cho CoreAPI.updateData()
        List<Table> tblList = new ArrayList<>();
        tblList.add(tbl);
        
        // gọi hàm updateData()
        api.updateData(tblList);
        
        System.out.println(api.download(resultPath));
    }
}

