package uet.jcia.core.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uet.jcia.constant.HbmConst;
import uet.jcia.data.node.ColumnNode;
import uet.jcia.data.node.MTORelationshipNode;
import uet.jcia.data.node.OTMRelationshipNode;
import uet.jcia.data.node.PrimaryKeyNode;
import uet.jcia.data.node.RootNode;
import uet.jcia.data.node.TableNode;
import uet.jcia.data.node.TreeNode;
import uet.jcia.utils.Helper;
import uet.jcia.utils.SqlTypeMapper;
import uet.jcia.utils.TreeDataHelper;

public class HbmParser implements Parser {
    
    private long tempIdGenerator = 0;
    
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    // prevent validating hbms
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
    
    
    public HbmParser(){
        cachedDocument = new HashMap<>();
        tableNameMapper = new HashMap<>();
        classNameMapper = new HashMap<>();
        pkNameMapper = new HashMap<>();
    }

    public TreeNode parse(List<String> xmlList){

        TreeNode root = new RootNode();
        List<TreeNode> tableNodes = new ArrayList<>();

        for(String xml : xmlList){
            List<TableNode> parsedResult = parseXmlFile(xml);
            for (TableNode tableNode : parsedResult) {
                tableNode.setParent(root);
                tableNode.setXmlPath(xml);
                tableNodes.add(tableNode);
            }
            
        }

        for (TreeNode tblNode : tableNodes) {
            List<TreeNode> childNodes = tblNode.getChilds();
            for (int j = 0; j < childNodes.size(); j++) {
                TreeNode childNode = childNodes.get(j);
                if (childNode instanceof MTORelationshipNode) {
                    MTORelationshipNode mtoNode = (MTORelationshipNode) childNode;
                    String referClass = mtoNode.getReferTable().getClassName();
                    String referColumnName = mtoNode.getReferColumn().getColumnName();
                    
                    TableNode referTable = (TableNode) Helper.deepClone(classNameMapper.get(referClass));
                    if (referTable != null) {
                        // remove unnecessary attributes
                        referTable.setChilds(null);
                        referTable.setXmlPath(null);
                        referTable.setHbmAttributes(null);
                        
                        mtoNode.setReferTable(referTable);
                        
                    } else { // use class name instead of table name
                        String referTableName = referClass.substring(referClass.lastIndexOf(".") + 1);
                        mtoNode.getReferTable().setTableName(referTableName);
                    }
                    
                    PrimaryKeyNode referColumn = pkNameMapper.get(mtoNode.getReferTable().getTableName() + "." + referColumnName);
                    if (referColumn != null) {
                        mtoNode.setReferColumn(referColumn);
                    } else {
                        referColumn = mtoNode.getReferColumn();
                    }
                    
                    // add foreign key for relationship
                    ColumnNode foreignKey = TreeDataHelper.generateForeignKey(referColumn);
//                        foreignKey.setParent(tblNode);
                    childNodes.add(foreignKey);
                    mtoNode.setForeignKey(foreignKey);
                    
                } else if (childNode instanceof OTMRelationshipNode) {
                    OTMRelationshipNode otmNode = (OTMRelationshipNode) childNode;
                    String referClass = otmNode.getReferTable().getClassName();
                    TableNode referTable = (TableNode) Helper.deepClone(classNameMapper.get(referClass));
                    if (referTable != null) {
                        referTable.setChilds(null);
                        referTable.setXmlPath(null);
                        referTable.setHbmAttributes(null);
                        
                        otmNode.setReferTable(referTable);
                    }
                }
            }
        }

        root.setChilds(tableNodes);
        root.setTempId(-1);
        return root;
    }
    

    public TreeNode parseXml(String xmlPath){
        List<String> xmlList = new ArrayList<>();
        xmlList.add(xmlPath);
        return parse(xmlList);
    }
    
    
    private List<TableNode> parseXmlFile(String xmlPath){
        List <TableNode> result = new ArrayList<>();
        try {
            File xmlFile = new File(xmlPath);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            // invalid hbm xml must contain hibernate-mapping tag
            if (doc.getElementsByTagName("hibernate-mapping").getLength() == 0) {
                return result;
            }
            
            // map xmlPath and document
            cachedDocument.put(xmlPath, doc);
            
            NodeList listClass = doc.getElementsByTagName(HbmConst.TAG_CLASS);
            
            // parse class tag
            for(int temp = 0; temp < listClass.getLength(); temp++){
                long tempId = generateId();
                Node classNode = listClass.item(temp);
//                ((Element)classNode).setAttribute(HBM_ATT_TEMP_ID, Long.toString(tempId));
                
                NamedNodeMap attributes = classNode.getAttributes();
                TableNode table = parseClassTag(classNode);
                table.setHbmAttributes(getAttrsFromNodeMap(attributes));
                table.setTempId(tempId);
//                table.setLinkedElement((Element)classNode);
                result.add(table);
            }
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
        String className = classElement.getAttribute(HbmConst.ATT_NAME);
        String tableName = classElement.getAttribute(HbmConst.ATT_TABLE);
        tableNode.setClassName(className);
        tableNode.setTableName(tableName);
        
        // add to cache
        classNameMapper.put(className, tableNode);
        tableNameMapper.put(tableName, tableNode);
        
        // child elements of the table
        List<TreeNode> childElements = new ArrayList<>();
        
        // get primary key
        Node idNode = classElement.getElementsByTagName(HbmConst.TAG_ID).item(0);
        if (idNode != null){
            NamedNodeMap pkAttrs = idNode.getAttributes();
            PrimaryKeyNode primaryKey = parseIdTag(idNode);
            primaryKey.setHbmAttributes(getAttrsFromNodeMap(pkAttrs));
//            primaryKey.setLinkedElement((Element)idNode);
//            primaryKey.setParent(tableNode);
            
            // add to cache
            pkNameMapper.put(tableName + "." + primaryKey.getColumnName(), primaryKey);
            
            childElements.add(primaryKey);
        }
        
        // get normal columns
        NodeList propertyNodes = classElement.getElementsByTagName(HbmConst.TAG_PROPERTY);
        for (int temp = 0; temp < propertyNodes.getLength(); temp++){
            Node propertyNode = propertyNodes.item(temp);
            
            NamedNodeMap propertyAttrs = propertyNode.getAttributes();
            ColumnNode columnNode = parsePropertyTag(propertyNode);
            columnNode.setHbmAttributes(getAttrsFromNodeMap(propertyAttrs));
//            columnNode.setLinkedElement((Element)propertyNode);
//            columnNode.setParent(tableNode);
            childElements.add(columnNode);
        }
        
        // get n-1 relationships
        NodeList mtoNodes = classElement.getElementsByTagName(HbmConst.TAG_MANY_TO_ONE);
        for(int temp = 0; temp < mtoNodes.getLength(); temp++){
            Node mtoNode = mtoNodes.item(temp);
            
            NamedNodeMap mtoAttrs = mtoNode.getAttributes();
            MTORelationshipNode relationship = parseManyToOneTag(mtoNode);
            relationship.setHbmAttributes(getAttrsFromNodeMap(mtoAttrs));
//            relationship.setLinkedElement((Element)mtoNode);
//            relationship.setParent(tableNode);
            childElements.add(relationship);
        }
        
        // get 1-n relationships
        NodeList setNodes = classElement.getElementsByTagName(HbmConst.TAG_SET);
        for(int temp = 0; temp < setNodes.getLength(); temp++){
            Node setNode = setNodes.item(temp);
            
            NamedNodeMap setAttrs = setNode.getAttributes();
            OTMRelationshipNode set = parseSet(setNode);
            set.setHbmAttributes(getAttrsFromNodeMap(setAttrs));
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
        idElement.setAttribute(HbmConst.ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        Element columnElement = (Element) idElement.getElementsByTagName(HbmConst.TAG_COLUMN).item(0);
        String lengthStr = columnElement.getAttribute(HbmConst.ATT_LENGTH);
        if (!lengthStr.isEmpty()) {
            primaryKey.setLength(Integer.parseInt(lengthStr));
        }
        
        primaryKey.setJavaName(idElement.getAttribute(HbmConst.ATT_NAME));
        primaryKey.setColumnName(columnElement.getAttribute(HbmConst.ATT_NAME));
        primaryKey.setDataType(SqlTypeMapper.getHbmtoSql(idElement.getAttribute(HbmConst.ATT_TYPE)));
        primaryKey.setPrimaryKey(true);
        primaryKey.setNotNull(true);
        
        // get generator
        Element generatorNode = (Element) idElement.getElementsByTagName(HbmConst.TAG_GENERATOR).item(0);
        if ("increment".equals(generatorNode.getAttribute(HbmConst.ATT_CLASS))){
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
        propertyElement.setAttribute(HbmConst.ATT_TEMP_ID, Long.toString(tempId));

        // get attributes
        Element columnElement = (Element) propertyElement.getElementsByTagName(HbmConst.TAG_COLUMN).item(0);
        String lengthStr = columnElement.getAttribute("length");
        if (!lengthStr.isEmpty()) {
            field.setLength(Integer.parseInt(lengthStr));
        }

        field.setJavaName(propertyElement.getAttribute(HbmConst.ATT_NAME));
        field.setColumnName(columnElement.getAttribute(HbmConst.ATT_NAME));
        field.setDataType(SqlTypeMapper.getHbmtoSql(propertyElement.getAttribute("type")));

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
        mtoElement.setAttribute(HbmConst.ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        TableNode referTable = new TableNode();
        PrimaryKeyNode referColumn = new PrimaryKeyNode();

        String referClass = mtoElement.getAttribute(HbmConst.ATT_CLASS);
        referTable.setClassName(referClass);

        Element columnElement = (Element) mtoElement.getElementsByTagName(HbmConst.TAG_COLUMN).item(0);
        String referColumName = columnElement.getAttribute(HbmConst.ATT_NAME);
        referColumn.setColumnName(referColumName);
        
        relationship.setJavaName(mtoElement.getAttribute(HbmConst.ATT_NAME));
        relationship.setReferTable(referTable);
        relationship.setReferColumn(referColumn);
        relationship.setType(HbmConst.TAG_MANY_TO_ONE);

        return relationship; 
    }
    
    private OTMRelationshipNode parseSet(Node setNode){
        OTMRelationshipNode relationship = new OTMRelationshipNode(); 
        Element setElement = (Element)setNode;
        
        // get temp id
        long tempId = generateId();
        relationship.setTempId(tempId);
        setElement.setAttribute(HbmConst.ATT_TEMP_ID, Long.toString(tempId));
        
        // get attributes
        TableNode referTable = new TableNode();
        ColumnNode foreignKey = new ColumnNode();
        
        Element keyElement = (Element) setElement.getElementsByTagName(HbmConst.TAG_KEY).item(0);
        Element columnElement = (Element) keyElement.getElementsByTagName(HbmConst.TAG_COLUMN).item(0);
        Element otmElement = (Element)setElement.getElementsByTagName(HbmConst.TAG_ONE_TO_MANY).item(0);
        
        foreignKey.setColumnName(columnElement.getAttribute(HbmConst.ATT_NAME));
        referTable.setTableName(setElement.getAttribute(HbmConst.ATT_TABLE));
        referTable.setClassName(otmElement.getAttribute(HbmConst.ATT_CLASS));
        
        relationship.setJavaName(setElement.getAttribute(HbmConst.ATT_NAME));
        relationship.setReferTable(referTable);
        relationship.setForeignKey(foreignKey);
        relationship.setType(HbmConst.TAG_ONE_TO_MANY);
        
        return relationship;
    }
    
    private HashMap<String, String> getAttrsFromNodeMap(NamedNodeMap nodeMap) {
        HashMap<String, String> attrs = new HashMap<>();
        for (int i = 0; i < nodeMap.getLength(); i++) {
            Attr attr = (Attr) nodeMap.item(i);
            attrs.put(attr.getName(), attr.getValue());
        }
        return attrs;
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
