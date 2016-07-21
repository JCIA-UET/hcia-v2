package cuong;

import java.util.List;
import java.util.Scanner;

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
        System.out.print("RefXml: ");
        tbl.setRefXml(scanner.nextLine());
        System.out.print("TempId: ");
        tbl.setTempId(scanner.nextLine());
        System.out.println("Table name: ");
        tbl.setTableName(scanner.nextLine());
        scanner.close();
        
        api.updateTable(tbl);
        
        System.out.println(api.download(resultPath));
    }

    public static void testXml() {
        String resultPath = api.parse("I:\\Workspace\\hcia-v2\\temp\\upload\\Category.hbm.xml");
        System.out.println("[temp data] " + resultPath);
        
        List<Table> tableList = api.getTableList(resultPath);
        
        System.out.println(tableList);
        
        Table tbl = new Table();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\n\n\tModify table");
        System.out.print("RefXml: ");
        tbl.setRefXml(scanner.nextLine());
        System.out.print("TempId: ");
        tbl.setTempId(scanner.nextLine());
        System.out.println("Table name: ");
        tbl.setTableName(scanner.nextLine());
        scanner.close();
        
        api.updateTable(tbl);
        
        System.out.println(api.download(resultPath));
    }
}

