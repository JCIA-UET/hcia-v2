package cuong;

import java.util.List;
import java.util.Scanner;

import uet.jcia.entities.Table;
import uet.jcia.model.CoreAPI;

public class TestCoreAPI {

    public static void main(String[] args) {
        CoreAPI api = new CoreAPI();
        
        String resultPath = api.parse("C:\\Users\\vy\\workspace\\hcia-v2\\temp\\upload\\vnu.zip");
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
        
        api.updateTable(tbl);
        
        System.out.println(api.download(resultPath));
    }
}
