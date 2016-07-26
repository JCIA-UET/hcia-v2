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
import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.MTORelationshipNode;
import uet.jcia.entities.OTMRelationshipNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.Relationship;
import uet.jcia.entities.RootNode;
import uet.jcia.entities.Table;
import uet.jcia.entities.TableNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.utils.Helper;
import uet.jcia.utils.Mappers;
import uet.jcia.utils.TreeDataHelper;

public class Parser {
    
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
    // table name - table node
    private HashMap<String, TableNode> tableNameMapper;
    // class name - table node
    private HashMap<String, TableNode> classNameMapper;
    // primary key name - primary key node
    private HashMap<String, PrimaryKeyNode> pkNameMapper;
    
    
    public Parser(){
        cachedDocument = new HashMap<>();
        tableNameMapper = new HashMap<>();
        classNameMapper = new HashMap<>();
        pkNameMapper = new HashMap<>();
    }
    
    public TreeNode parseXmlList(List<String> xmlList){
        
//        System.out.println("[Parser] starting...");

        TreeNode root = new RootNode();
        List<TreeNode> tableNodes = new ArrayList<>();
        
        for(String xml : xmlList){
            TableNode tableNode = parseXmlFile(xml);
            tableNode.setParent(root);
            tableNode.setXmlPath(xml);
            tableNodes.add(tableNode);
        }
        
//        System.out.println("[Parser] add relationships...");
        for (TreeNode tblNode : tableNodes) {
            List<TreeNode> childNodes = tblNode.getChilds();
            for (int j = 0; j < childNodes.size(); j++) {
                TreeNode childNode = childNodes.get(j);
                if (childNode instanceof MTORelationshipNode) {
                    MTORelationshipNode mtoNode = (MTORelationshipNode) childNode;
                    String referClass = mtoNode.getReferTable().getClassName();
                    String referColumnName = mtoNode.getReferColumn().getColumnName();
                    
                    TableNode referTable = (TableNode) Helper.deepClone(classNameMapper.get(referClass));
                    referTable.setChilds(null);
                    PrimaryKeyNode referColumn = pkNameMapper.get(referTable.getTableName() + "." + referColumnName);
                    mtoNode.setReferTable(referTable);
                    mtoNode.setReferColumn(referColumn);
                    
                    // add foreign key for relationship
                    ColumnNode foreignKey = TreeDataHelper.generateForeignKey(referColumn);
                    foreignKey.setParent(tblNode);
                    childNodes.add(foreignKey);
                }
            }
        }
        
//        System.out.println("[Parser] done!");
        root.setChilds(tableNodes);
        root.setTempId(-1);
        return root;
    }

    public TreeNode parseXml(String xmlPath){
        List<String> xmlList = new ArrayList<>();
        xmlList.add(xmlPath);
        return parseXmlList(xmlList);
    }
    
    
    private TableNode parseXmlFile(String xmlPath){
        TableNode result = new TableNode();
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
                long tempId = generateId();
                Node classNode = listClass.item(temp);
                ((Element)classNode).setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));
                
                result = parseClassTag(classNode);
                result.setTempId(tempId);
                result.setLinkedElement((Element)classNode);
            }
            
            System.out.println("[Parser] analylised....");
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private TableNode parseClassTag(Node classNode){
        TableNode tableNode = new TableNode();
        Element classElement = (Element)classNode;
        
        // attribute for class tag
        String className = classElement.getAttribute("name");
        String tableName = classElement.getAttribute("table");
        tableNode.setClassName(className);
        tableNode.setTableName(tableName);
        
        // add to cache
        classNameMapper.put(className, tableNode);
        tableNameMapper.put(tableName, tableNode);
        
        // child elements of the table
        List<TreeNode> childElements = new ArrayList<>();
        
        // get primary key
        Node idNode = classElement.getElementsByTagName("id").item(0);
        if (idNode != null){
            PrimaryKeyNode primaryKey = parseIdTag(idNode);
            primaryKey.setLinkedElement((Element)idNode);
            primaryKey.setParent(tableNode);
            
            // add to cache
            pkNameMapper.put(tableName + "." + primaryKey.getColumnName(), primaryKey);
            
            childElements.add(primaryKey);
        }
        
        // get normal columns
        NodeList propertyNodes = classElement.getElementsByTagName("property");
        for (int temp = 0; temp < propertyNodes.getLength(); temp++){
            Node propertyNode = propertyNodes.item(temp);
            ColumnNode columnNode = parsePropertyTag(propertyNode);
            columnNode.setLinkedElement((Element)propertyNode);
            columnNode.setParent(tableNode);
            childElements.add(columnNode);
        }
        
        // get n-1 relationships
        NodeList mtoNodes = classElement.getElementsByTagName("many-to-one");
        for(int temp = 0; temp < mtoNodes.getLength(); temp++){
            Node mtoNode = mtoNodes.item(temp);
            MTORelationshipNode relationship = parseManyToOneTag(mtoNode);
            relationship.setLinkedElement((Element)mtoNode);
            relationship.setParent(tableNode);
            childElements.add(relationship);
        }
        
        // get 1-n relationships
        NodeList setNodes = classElement.getElementsByTagName("set");
        for(int temp = 0; temp < setNodes.getLength(); temp++){
            Node setNode = setNodes.item(temp);
            OTMRelationshipNode set = parseSet(setNode);
            set.setLinkedElement((Element)setNode);
            set.setParent(tableNode);
            childElements.add(set);
        }
        
        // add columns, relationships to table
        tableNode.setChilds(childElements);
        return tableNode;
    }
    
    private PrimaryKeyNode parseIdTag(Node idNode){
        PrimaryKeyNode primaryKey = new PrimaryKeyNode();
        Element idElement = (Element) idNode;
        
        // add temp id
        long tempId = generateId();
        primaryKey.setTempId(tempId);
        idElement.setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        Element columnElement = (Element) idElement.getElementsByTagName("column").item(0);
        primaryKey.setColumnName(columnElement.getAttribute("name"));
        String lengthStr = columnElement.getAttribute("length");
        if (!lengthStr.isEmpty()) {
            primaryKey.setLength(Integer.parseInt(lengthStr));
        }
        primaryKey.setDataType(Mappers.getHbmtoSql(idElement.getAttribute("type")));
        
        primaryKey.setPrimaryKey(true);
        primaryKey.setNotNull(true);
        
        // get generator
        Element generatorNode = (Element) idElement.getElementsByTagName("generator").item(0);
        if ("increment".equals(generatorNode.getAttribute("class"))){
            primaryKey.setAutoIncrement(true);
        }
        
        return primaryKey;
    }
    
    private ColumnNode parsePropertyTag(Node propertyNode){
        ColumnNode field = new ColumnNode();
        Element propertyElement = (Element) propertyNode;
        
        // set temp id
        long tempId = generateId();
        field.setTempId(tempId);
        propertyElement.setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));

        // get attributes
        Element columnElement = (Element) propertyElement.getElementsByTagName("column").item(0);
        field.setColumnName(columnElement.getAttribute("name"));
        String lengthStr = columnElement.getAttribute("length");
        if (!lengthStr.isEmpty()) {
            field.setLength(Integer.parseInt(lengthStr));
        }
        field.setDataType(Mappers.getHbmtoSql(propertyElement.getAttribute("type")));

        String notNull = columnElement.getAttribute("not-null");
        if ("true".equals(notNull)) {
            field.setNotNull(true);
        }
        
        return field;
    }
    
    private MTORelationshipNode parseManyToOneTag(Node mtoNode){
        MTORelationshipNode relationship = new MTORelationshipNode();
        Element mtoElement = (Element)mtoNode;
        
        // set temp id
        long tempId = generateId();
        relationship.setTempId(tempId);
        mtoElement.setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        TableNode referTable = new TableNode();
        PrimaryKeyNode referColumn = new PrimaryKeyNode();

        String referClass = mtoElement.getAttribute("class");
        referTable.setClassName(referClass);

        Element columnElement = (Element) mtoElement.getElementsByTagName("column").item(0);
        String referColumName = columnElement.getAttribute("name");
        referColumn.setColumnName(referColumName);
        
        relationship.setReferTable(referTable);
        relationship.setReferColumn(referColumn);

        return relationship; 
    }
    
    private OTMRelationshipNode parseSet(Node setNode){
        OTMRelationshipNode relationship = new OTMRelationshipNode(); 
        Element setElement = (Element)setNode;
        
        // get temp id
        long tempId = generateId();
        relationship.setTempId(tempId);
        setElement.setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        TableNode referTable = new TableNode();
        ColumnNode foreignKey = new ColumnNode();
        
        Element keyElement = (Element) setElement.getElementsByTagName("key").item(0);
        Element columnElement = (Element) keyElement.getElementsByTagName("column").item(0);
        Element otmElement = (Element)setElement.getElementsByTagName("one-to-many").item(0);
        
        foreignKey.setColumnName(columnElement.getAttribute("name"));
        referTable.setTableName(setElement.getAttribute("table"));
        referTable.setClassName(otmElement.getAttribute("class"));
        
        relationship.setReferTable(referTable);
        
        return relationship;
    }

    private long generateId() {
        return tempIdGenerator++;
    }
    
    public HashMap<String, Document> getCachedDocument() {
        return cachedDocument;
    }
    
    public Document getDocumentByXmlPath(String xmlPath) {
        return cachedDocument.get(xmlPath);
    }
}
