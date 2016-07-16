/* File : Parser.class 
 * Author : hieusonson9x@gmail.com
 * Details: to  parser XML to List<Table> . Key API parserAllToListTable 
 * */

package uet.jcia.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uet.jcia.entities.Column;
import uet.jcia.entities.Relationship;
import uet.jcia.entities.Table;

public class Parser {
	private HashMap<String,String> mappingType;
	
	public Parser(){
		mappingType = new HashMap<>();
		mappingType.put("java.lang.Integer", "INTEGER");
		mappingType.put("int", "INTEGER");
		mappingType.put("java.lang.Long", "BIGINT");
		mappingType.put("long", "BIGINT");
		mappingType.put("java.lang.Short", "SMALLINT");
		mappingType.put("short", "SMALLINT");
		mappingType.put("java.lang.Float", "FLOAT");
		mappingType.put("float", "FLOAT");
		mappingType.put("java.lang.Double", "DOUBLE");
		mappingType.put("double", "DOUBLE");
		mappingType.put("java.math.BigDecimal", "NUMERIC");
		mappingType.put("java.lang.String", "VARCHAR");
		mappingType.put("string", "VARCHAR");
		mappingType.put("java.lang.Byte", "TINYINT");
		mappingType.put("byte", "TINYINT");
		mappingType.put("java.lang.Boolean", "BIT");
		mappingType.put("boolean", "BIT");
		mappingType.put("java.sql.Date", "DATE");
		mappingType.put("date", "DATE");
		mappingType.put("java.util.Date", "DATE");
		mappingType.put("java.sql.Time", "TIME");
		mappingType.put("java.sql.Timestamp", "TIMESTAMP");
		mappingType.put("java.util.Calendar", "TIMESTAMP");
		mappingType.put("byte[]", "VARBINARY()");
		mappingType.put("java.io.Serializable", "BLOB()");
		mappingType.put("java.sql.Clob", "CLOB()");
		mappingType.put("java.sql.Blob", "BLOB()");
		mappingType.put("java.lang.Class", "VARCHAR");
		mappingType.put("java.util.Locale", "VARCHAR");
		mappingType.put("java.util.TimeZone", "VARCHAR");
		mappingType.put("java.util.Currency", "VARCHAR");	
	}
	
	
	// key API for controller layer 
	public List<Table> parseAllToListTable(List<String> xmlLinkList){
		List<Table> result = new ArrayList<Table>();
		
		// add list table to result with each file *.hbm.xml
		for(String iter:xmlLinkList){
			result.addAll(parseOneXmlToListTable(iter));
		}
		
		return result;
	}
	
	
	private List<Table> parseOneXmlToListTable(String xmlLink){
		List<Table> result = new ArrayList<>();
		try {
			File fXmlFile = new File(xmlLink);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList listClass = doc.getElementsByTagName("class");
			
			// add one table to result with each class tag
			for(int temp = 0; temp < listClass.getLength(); temp++){
				Table table = parseOneTagClassToTable(listClass.item(temp));
				result.add(table);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Table parseOneTagClassToTable(Node classNode){
		Table result = new Table();
		//write code here
		Element element = (Element)classNode;
		result.setTableName(element.getAttribute("table").toUpperCase());
		
		
		//add list column to Table result
		List<Column> columns = new ArrayList<>();
		
		// Add column Id if have tag
		NodeList id = element.getElementsByTagName("id");
		if(id.getLength()!=0){
			columns.add(parseIdTagToColumn(id.item(0)));
		}
		
		//add list column PK if have tag composite-id
		NodeList composite = element.getElementsByTagName("composite-id");
		if(composite.getLength() != 0){
			columns.addAll(parserOneCompositeIdTagToListColumn(composite.item(0)));
		}
		
		//add list column by property tags
		NodeList pNodeList = element.getElementsByTagName("property");
		for(int temp = 0; temp < pNodeList.getLength(); temp++){
			columns.add(parseOnePropertyTagToColumn(pNodeList.item(temp)));
		}
		
		// add list relationship
		List<Relationship> relationships = new ArrayList<>();
		NodeList r1NodeList = element.getElementsByTagName("many-to-one");
		for(int temp = 0; temp < r1NodeList.getLength(); temp++){
			relationships.add(parseOneTagManyToOne(r1NodeList.item(temp)));
		
		}
		NodeList r2NodeList = element.getElementsByTagName("set");
		for(int temp = 0; temp < r2NodeList.getLength(); temp++){
			relationships.add(parseOneTagOneToMany(r2NodeList.item(temp)));
		}
		
		//set column and relation to table result
		result.setListRelationship(relationships);
		result.setListColumn(columns);
		return result;
	}
	
	private Column parseIdTagToColumn(Node idNode){
		if(idNode== null ){ return null;}
		
		Column result = new Column();
		Element element = (Element) idNode;
		
		//get Attribute name and type ,primary key , not null property
		Element col = (Element) element.getElementsByTagName("column").item(0);
		result.setName(col.getAttribute("name").toUpperCase());
		result.setLength(col.getAttribute("length"));
		
		result.setType(mappingType.get(element.getAttribute("type")));
		result.setPrimaryKey(true);
		result.setNot_null(true);
		
		//get generator
		 Element generatorNode = (Element) element.getElementsByTagName("generator").item(0);
		 String test =  generatorNode.getAttribute("class");
		 if(!test.equals("select")){
			 result.setAutoIcrement(true);
		 }
		 else result.setAutoIcrement(false);
		return result;
	}
	
	private Column parseOnePropertyTagToColumn(Node pNode){
		Column result = new Column();
		Element element = (Element) pNode;
		
		//set name , type , primary key and AutoIcrement
		Element col = (Element) element.getElementsByTagName("column").item(0);
		result.setName(col.getAttribute("name").toUpperCase());
		result.setLength(col.getAttribute("length"));

		result.setType(mappingType.get(element.getAttribute("type")));
		result.setPrimaryKey(false);
		result.setAutoIcrement(false);
		
		//set not-null
		Element generatorNode = (Element) element.getElementsByTagName("column").item(0);
		String test = generatorNode.getAttribute("not-null");
		if(test.equals("true")){
			result.setNot_null(true);
		} else result.setNot_null(false);
		
		return result;
	}
	
	private Relationship parseOneTagManyToOne(Node rNode){
		Relationship result = new Relationship();
		Element element = (Element)rNode;
		result.setReferTable(element.getAttribute("name").toUpperCase());
		result.setType("manytoone");
		
		Element col = (Element) element.getElementsByTagName("column").item(0);
		result.setJoinColumn(col.getAttribute("name").toUpperCase());
		
		return result; 
	}
	
	private Relationship parseOneTagOneToMany(Node rNode){
		Relationship result = new Relationship(); 
		result.setType("onetomany");
		
		Element element = (Element)rNode;
		result.setReferTable(element.getAttribute("table").toUpperCase());
		
		Element key = (Element) element.getElementsByTagName("key").item(0);
		Element col = (Element) key.getElementsByTagName("column").item(0);
		
		result.setJoinColumn(col.getAttribute("name").toUpperCase());
		
		return result;
	}
	
	private List<Column> parserOneCompositeIdTagToListColumn(Node cpNode){
		if(cpNode == null ) return null;
		List<Column> result = new ArrayList<>();
		Element element = (Element)cpNode;
		NodeList listKey = element.getElementsByTagName("key-property");
		
		// create one column by each key-property tag
		for(int temp = 0 ; temp < listKey.getLength() ; temp++){
			Element e = (Element)listKey.item(temp);
			Column column = new Column();
			
			// set type, default not-null , default PK , default AI
			column.setType(mappingType.get(e.getAttribute("type")));
			column.setNot_null(true);
			column.setPrimaryKey(true);
			column.setAutoIcrement(false);
			
			Element col = (Element) e.getElementsByTagName("column").item(0);
			column.setName(col.getAttribute("name"));
			column.setLength(col.getAttribute("length"));
			result.add(column);
		}
		return result;
	}
	
}
