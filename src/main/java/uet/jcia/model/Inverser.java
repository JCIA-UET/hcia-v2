package uet.jcia.model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;

public class Inverser {
    
    private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public Inverser() {
        try {
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void updateHbmClass(Table table) {
        try {
            File xmlFile = new File(table.getRefXml());
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            System.out.println("[Inverser] parsing [" + table.getRefXml() + "]...");
            Document doc = dBuilder.parse(xmlFile);
            doc.normalize();
            
            Element rootNode = doc.getDocumentElement();
            NodeList classNodes = rootNode.getElementsByTagName("class");
            
            System.out.println("[Inverser] modifying...");
            for (int count = 0; count < classNodes.getLength(); count++) {
                Element e = (Element) classNodes.item(count);
                if (e.getAttribute("name").equalsIgnoreCase(table.getClassName())) {
                    e.setAttribute("table", table.getTableName());
                }
            }
            
            System.out.println("[Inverser] saving...");
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(table.getRefXml()));
            OutputFormat format = new OutputFormat();
            format.setStandalone(true);
            serializer.setOutputFormat(format);
            serializer.serialize(doc);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void updateHbmProperty(Column column) {
        try {
            File xmlFile = new File(column.getRefXml());
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            System.out.println("[Inverser] parsing [" + column.getRefXml() + "]...");
            Document doc = dBuilder.parse(xmlFile);
            doc.normalize();
            
            Element rootNode = doc.getDocumentElement();
            
            // column means in mySQL, it may be property tag
            NodeList columnNodes = rootNode.getElementsByTagName("property");
            
            System.out.println("[Inverser] modifying...");
            for (int count = 0; count < columnNodes.getLength(); count++) {
                Element e = (Element) columnNodes.item(count);
                if (e.getAttribute("name").equalsIgnoreCase(column.getMappingName())) {
                    if (column.getType() != null) {
                        e.setAttribute("type", column.getType());
                    }
                    if (column.getLength() != null) {
                        e.setAttribute("length", column.getLength());
                    }
                    
                    Element childCol = (Element) e.getElementsByTagName("column").item(0);
                    if (column.getName() != null) {
                        childCol.setAttribute("name", column.getName());
                    }
                    if (column.isNotNull()) {
                        childCol.setAttribute("not-null", "true");
                    } else {
                        childCol.setAttribute("not-null", "false");
                    }
                    
                }
            }
            
            System.out.println("[Inverser] saving...");
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(column.getRefXml()));
            OutputFormat format = new OutputFormat();
            format.setStandalone(true);
            serializer.setOutputFormat(format);
            serializer.serialize(doc);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void updateHbmId(Column column) {
        try {
            File xmlFile = new File(column.getRefXml());
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            System.out.println("[Inverser] parsing [" + column.getRefXml() + "]...");
            Document doc = dBuilder.parse(xmlFile);
            doc.normalize();
            
            Element rootNode = doc.getDocumentElement();
            
            // column means in mySQL, it may be property tag
            Element idNode = (Element) rootNode.getElementsByTagName("id").item(0);
            
            System.out.println("[Inverser] modifying...");
            if (idNode.getAttribute("name").equals(column.getMappingName())) {
                if (column.getType() != null) {
                    idNode.setAttribute("type", column.getType());
                }
                if (column.getLength() != null) {
                    idNode.setAttribute("length", column.getLength());
                }
                
                Element childCol = (Element) idNode.getElementsByTagName("column").item(0);
                if (column.getName() != null) {
                    childCol.setAttribute("name", column.getName());
                }
                if (column.isNotNull()) {
                    childCol.setAttribute("not-null", "true");
                } else {
                    childCol.setAttribute("not-null", "false");
                }
                
                Element childGen = (Element) idNode.getElementsByTagName("generator").item(0);
                if (column.isAutoIncrement()) {
                    childGen.setAttribute("class", "increment");
                } else {
                    childGen.setAttribute("class", "assigned");
                }
            }
            
            System.out.println("[Inverser] saving...");
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(column.getRefXml()));
            OutputFormat format = new OutputFormat();
            format.setStandalone(true);
            serializer.setOutputFormat(format);
            serializer.serialize(doc);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
