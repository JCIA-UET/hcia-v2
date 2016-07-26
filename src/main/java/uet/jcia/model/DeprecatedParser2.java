package uet.jcia.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uet.jcia.entities.Column;
import uet.jcia.entities.Relationship;
import uet.jcia.entities.Table;
import uet.jcia.utils.Helper;
import uet.jcia.utils.Mappers;

public class DeprecatedParser2 {
    
    private long tempIdGenerator = 0;
    
    public static final String ONE_TO_ONE = "one-to-one";
    public static final String ONE_TO_MANY = "one-to-many";
    public static final String MANY_TO_ONE = "many-to-one";

    public static final String HBM_HIBERNATE_MAPPING = "hibernate-mapping";
    public static final String HBM_CLASS = "class";
    
    public static final String HBM_ID = "id";
    public static final String HBM_GENERATOR = "generator";
    public static final String HBM_PROPERTY = "property";
    public static final String HBM_SET = "set";
    public static final String HBM_COLUMN = "column";
    public static final String HBM_ONE_TO_MANY = "one-to-many";
    public static final String HBM_MANY_TO_ONE = "many-to-one";
    
    public static final String HBM_ATT_TEMP_ID = "tempid";
    public static final String HBM_ATT_NAME = "name";
    public static final String HBM_ATT_TABLE = "table";
    
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    
    static {
        try {
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    // refxml - Document
    private HashMap<String, Document> cachedDocument;
    // temporary id - table
    private HashMap<String, Table> cachedTable;
    // temporary id - column
    private HashMap<String, Column> cachedColumn;
    // temporary id - relationship
    private HashMap<String, Relationship> cachedRelationship;
    
    // table name - temporary id
    private HashMap<String, String> tableNameIdMapper;
    // class name - temporary id
    private HashMap<String, String> classNameIdMapper;
    // column name - temporary id
    private HashMap<String, String> columnNameIdMapper;
    
    public DeprecatedParser2(){
        cachedDocument = new HashMap<>();
        cachedTable = new HashMap<>();
        cachedColumn = new HashMap<>();
        cachedRelationship = new HashMap<>();
        
        tableNameIdMapper = new HashMap<>();
        classNameIdMapper = new HashMap<>();
        columnNameIdMapper = new HashMap<>();
    }
    
    public List<Table> parseXmlList(List<String> xmlList){
        
        System.out.println("[Parser] starting...");

        List<Table> tableList = new ArrayList<Table>();
        
        for(String xml : xmlList){
            tableList.addAll(parseXmlFile(xml));
        }
        
        System.out.println("[Parser] add relationships...");
        for (Table tbl : tableList) {
            for (Relationship r : tbl.getListRelationship()) {
                if (MANY_TO_ONE.equals(r.getType())) {
                    Column referColumn = cachedColumn.get(
                            columnNameIdMapper.get(r.getReferColumn().getName()));
                    Table referTable = cachedTable.get(
                            classNameIdMapper.get(r.getReferTable().getClassName()));
                    
                    if (referTable != null) {
                        r.setReferTable(referTable);
                    }
                    
                    // add foreign keys
                    if (referColumn != null) {
                        Column foreignKey = (Column) Helper.deepClone(referColumn);
                        foreignKey.setPrimaryKey(false);
                        foreignKey.setForeignKey(true);
                        foreignKey.setTableId(r.getTableId());
                        
                        tbl.getListColumn().add(foreignKey);
                    }
                }
            }
        }
        
        System.out.println("[Parser] done!");
        return tableList;
    }

    public List<Table> parseXml(String xmlPath){
        List<String> xmlList = new ArrayList<>();
        xmlList.add(xmlPath);
        return parseXmlList(xmlList);
    }
    
    
    private List<Table> parseXmlFile(String xmlPath){
        List<Table> result = new ArrayList<>();
        try {
            File xmlFile = new File(xmlPath);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            
            System.out.println("[Parser] parsing [" + xmlPath + "]....");
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            System.out.println("[Parser] parsed....");
            
            System.out.println("[Parser] analylising....");
            // invalid hbm xml must contain hibernate-mapping tag
            if (doc.getElementsByTagName("hibernate-mapping").getLength() == 0) {
                return result;
            }
            
            // map xmlPath and document
            cachedDocument.put(xmlPath, doc);
            
            NodeList listClass = doc.getElementsByTagName("class");
            
            // parse class tag
            for(int temp = 0; temp < listClass.getLength(); temp++){
                String tempId = generateId();
                Node classNode = listClass.item(temp);
                ((Element)classNode).setAttribute(HBM_ATT_TEMP_ID, tempId);
                
                Table table = parseClassTag(classNode);
                table.setRefXml(xmlPath);
                table.setTempId(tempId);
                
                result.add(table);
                tableNameIdMapper.put(table.getTableName(), tempId);
                classNameIdMapper.put(table.getClassName(), tempId);
                cachedTable.put(tempId, table);
            }
            
            System.out.println("[Parser] analylised....");
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Table parseClassTag(Node classNode){
        Table result = new Table();
        Element classElement = (Element)classNode;
        
        // attribute for class tag
        String className = classElement.getAttribute("name");
        String tableName = classElement.getAttribute("table");
        result.setClassName(className);
        result.setTableName(tableName);
        
        // get columns of the table
        List<Column> columns = new ArrayList<>();
        
        // get primary key
        Node idNode = classElement.getElementsByTagName("id").item(0);
        if (idNode != null){
            Column primaryKey = parseIdTag(idNode);
            primaryKey.setTableId(classElement.getAttribute(HBM_ATT_TEMP_ID));
            columns.add(primaryKey);
        }
        
        // get normal columns
        NodeList propertyNodes = classElement.getElementsByTagName("property");
        for (int temp = 0; temp < propertyNodes.getLength(); temp++){
            Column column = parsePropertyTag(propertyNodes.item(temp));
            column.setTableId(classElement.getAttribute(HBM_ATT_TEMP_ID));
            columns.add(column);
        }
        
        // get relationships for table
        List<Relationship> relationships = new ArrayList<>();
        
        // get n-1 relationships
        NodeList mtoNodes = classElement.getElementsByTagName("many-to-one");
        for(int temp = 0; temp < mtoNodes.getLength(); temp++){
            Relationship relationship = parseManyToOneTag(mtoNodes.item(temp));
            relationship.setTableId(classElement.getAttribute(HBM_ATT_TEMP_ID));
            relationships.add(relationship);
        }
        
        // get 1-n relationships
        NodeList setNodes = classElement.getElementsByTagName("set");
        for(int temp = 0; temp < setNodes.getLength(); temp++){
            Relationship set = parseSet(setNodes.item(temp));
            set.setTableId(classElement.getAttribute(HBM_ATT_TEMP_ID));
            relationships.add(set);
        }
        
        // add columns, relationships to table
        result.setListRelationship(relationships);
        result.setListColumn(columns);
        return result;
    }
    
    private Column parseIdTag(Node idNode){
        Column primaryKey = new Column();
        Element idElement = (Element) idNode;
        
        // add temp id
        String tempId = generateId();
        primaryKey.setTempId(tempId);
        idElement.setAttribute(HBM_ATT_TEMP_ID, tempId);
        
        // get attributes
        Element columnElement = (Element) idElement.getElementsByTagName("column").item(0);
        primaryKey.setName(columnElement.getAttribute("name"));
        primaryKey.setLength(columnElement.getAttribute("length"));
        primaryKey.setType(Mappers.getHbmtoSql(idElement.getAttribute("type")));
        
        primaryKey.setPrimaryKey(true);
        primaryKey.setNotNull(true);
        
        // get generator
        Element generatorNode = (Element) idElement.getElementsByTagName("generator").item(0);
        if ("increment".equals(generatorNode.getAttribute("class"))){
            primaryKey.setAutoIncrement(true);
        }
        
        columnNameIdMapper.put(primaryKey.getName(), tempId);
        cachedColumn.put(tempId, primaryKey);
        
        return primaryKey;
    }
    
    private Column parsePropertyTag(Node propertyNode){
        Column field = new Column();
        Element propertyElement = (Element) propertyNode;
        
        // set temp id
        String tempId = generateId();
        field.setTempId(tempId);
        propertyElement.setAttribute(HBM_ATT_TEMP_ID, tempId);

        // get attributes
        Element columnElement = (Element) propertyElement.getElementsByTagName("column").item(0);
        field.setName(columnElement.getAttribute("name"));
        field.setLength(columnElement.getAttribute("length"));
        field.setType(Mappers.getHbmtoSql(propertyElement.getAttribute("type")));

        if ("true".equals(columnElement.getAttribute("not-null"))) {
            field.setNotNull(true);
        }
        
        columnNameIdMapper.put(field.getName(), tempId);
        cachedColumn.put(tempId, field);
        
        return field;
    }
    
    private Relationship parseManyToOneTag(Node mtoNode){
        Relationship relationship = new Relationship();
        Element mtoElement = (Element)mtoNode;
        
        // set temp id
        String tempId = generateId();
        relationship.setTempId(tempId);
        mtoElement.setAttribute(HBM_ATT_TEMP_ID, tempId);
        
        // get attributes
        String referClass = mtoElement.getAttribute("class");
        Table referTable = new Table();
        referTable.setClassName(referClass);
        // true unique attribute makes relationship become an 1-1 relationship 
        String unique = mtoElement.getAttribute("unique");
        Element columnElement = (Element) mtoElement.getElementsByTagName("column").item(0);
        String referColumName = columnElement.getAttribute("name");
        Column referColumn = new Column();
        referColumn.setName(referColumName);
        
        relationship.setReferTable(referTable);
        relationship.setReferColumn(referColumn);

        if (unique != null && unique.equalsIgnoreCase("true")) {
            relationship.setType(ONE_TO_ONE);
        } else {
            relationship.setType(MANY_TO_ONE);
        }
        
        cachedRelationship.put(tempId, relationship);
        return relationship; 
    }
    
    private Relationship parseSet(Node setNode){
        Relationship relationship = new Relationship(); 
        Element setElement = (Element)setNode;
        
        // get temp id
        String tempId = generateId();
        relationship.setTempId(tempId);
        setElement.setAttribute(HBM_ATT_TEMP_ID, tempId);
        
        // get attributes
        Table referTable = new Table();
        Column referColumn = new Column();
        referTable.setTableName(setElement.getAttribute("table"));
        Element keyElement = (Element) setElement.getElementsByTagName("key").item(0);
        Element columnElement = (Element) keyElement.getElementsByTagName("column").item(0);
        referColumn.setName(columnElement.getAttribute("name"));
        
        Element otmElement = (Element)setElement.getElementsByTagName("one-to-many").item(0);
        referTable.setClassName(otmElement.getAttribute("class"));
        
        relationship.setReferTable(referTable);
        relationship.setReferColumn(referColumn);
        // set type
        relationship.setType(ONE_TO_MANY);
        
        cachedRelationship.put(tempId, relationship);
        return relationship;
    }

    private String generateId() {
        return "" + tempIdGenerator++;
    }
    
    public HashMap<String, Document> getCachedDocument() {
        return cachedDocument;
    }
    
    public Table getTableByName(String tableName) {
        return cachedTable.get(tableNameIdMapper.get(tableName));
    }
    
    public Document getDocumentByXmlPath(String xmlPath) {
        return cachedDocument.get(xmlPath);
    }
}
