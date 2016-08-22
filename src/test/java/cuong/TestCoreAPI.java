package cuong;

import java.io.File;
import java.util.List;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;
import uet.jcia.utils.JsonHelper;

public class TestCoreAPI {

    static CoreAPI api = new CoreAPI();
    
    public static void main(String[] args) throws Exception {
//        testZip();
//        testXml();
//    	testJava();
    	fileTest();
    }
    
    public static void fileTest() {
    	File javaFile = new File("temp\\upload\\classicmodels.java");
    	System.out.println(javaFile.getParentFile().getAbsolutePath());
    	String fileName = javaFile.getName().replace(".java", ".hbm.xml");
    	System.out.println(fileName);
    }
    
    public static void testJava() throws Exception {
        String resultPath = api.parse("temp\\upload\\classicmodels.zip");
        System.out.println("[temp data] " + resultPath);
        
        TreeNode rootNode = api.getParsedData(resultPath);
        System.out.println(JsonHelper.toJsonString(rootNode));
    }
    
    /*public static void testZip() {
        String resultPath = api.parse("I:\\Workspace\\hcia-v2\\temp\\upload\\vnu.zip");
        System.out.println("[temp data] " + resultPath);
        
        List<Table> tableList = api.getTableList(resultPath);
        
        System.out.println(tableList);
        
        Table modifiedTable = tableList.get(0);
        Table tbl = new Table();
        
        System.out.println("\n\n\n\tModify table");
        System.out.println("RefXml: " + modifiedTable.getRefXml());
        tbl.setRefXml(modifiedTable.getRefXml());
        System.out.println("TempId: " + modifiedTable.getTempId());
        tbl.setTempId(modifiedTable.getTempId());
        System.out.println("New Table name: " + "NEW_TABLE");
        tbl.setTableName("NEW_TABLE");
        
        Column modifiedPk = modifiedTable.getListColumn().get(0);
        Column newPk = new Column();
        System.out.println("\n\n\tModify PK");
        System.out.println("TempId: " + modifiedPk.getTempId());
        newPk.setTempId(modifiedPk.getTempId());
        System.out.println("New column name: " + "NEW_ID");
        newPk.setName("NEW_ID");
        System.out.println("Auto increment: " + "true");
        newPk.setAutoIncrement(true);
        
        List<Column> columns = new ArrayList<>();
        columns.add(newPk);
        tbl.setListColumn(columns);

        Relationship modifiedRela = modifiedTable.getListRelationship().get(0);
        Relationship rela = new Relationship();
        
        System.out.println("\n\n\n\tModify relationship");
        System.out.println("TempId: " + modifiedRela.getTempId());
        rela.setTempId(modifiedRela.getTempId());
        
        System.out.println("Refer tableId: " + "45");
        Table referTable = getTableById(tableList, "45");
        rela.setReferTable(referTable);
        
        System.out.println("Refer column id: " + "46");
        Column referColumn = getCoumnById(referTable.getListColumn(), "46");
        rela.setReferColumn(referColumn);
        
        System.out.println("Type: " + modifiedRela.getType());
        rela.setType(modifiedRela.getType());
        
        List<Relationship> relaList = new ArrayList<>();
        relaList.add(rela);
        tbl.setListRelationship(relaList);
        
        List<Table> updatedTableList = new ArrayList<>();
        updatedTableList.add(tbl);
        
        CoreAPI api2 = new CoreAPI();
        api2.updateData(updatedTableList, resultPath);
        
        CoreAPI api3 = new CoreAPI();
        System.out.println(api3.download(resultPath));
    }*/
    

   /* public static void testXml() {
        String resultPath = api.parse("temp\\upload\\OrderItem.hbm.xml");
        System.out.println("[temp data] " + resultPath);
        
        TreeNode rootNode = api.getParsedData(resultPath);
        System.out.println(rootNode);
        
        api.updateData(rootNode.getChilds().get(0));
        
        String updatedPath = api.refresh(resultPath);
        System.out.println("[updated path] " + updatedPath);
        
        rootNode = api.getParsedData(updatedPath);
        System.out.println(rootNode);
        
        System.out.println(api.download(updatedPath));
    }*/
    
    public static Table getTableById(List<Table> tables, String tableId) {
        for (Table tbl : tables) {
            if (tableId.equals(tbl.getTempId())) {
                return tbl;
            }
        }
        return null;
    }
    
    public static Column getCoumnById(List<Column> columns, String columnId) {
        for (Column col : columns) {
            if (columnId.equals(col.getTempId())) {
                return col;
            }
        }
        return null;
    }
}

