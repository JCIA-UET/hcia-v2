package uet.jcia.model;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.catalina.filters.RemoteIpFilter.XForwardedRequest;
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
    
    public void updateHbmClass(Table tbl, Document doc) {
        if (tbl == null || doc == null) return;
        
        Element rootNode = doc.getDocumentElement();
        NodeList classNodes = rootNode.getElementsByTagName("class");
        
        System.out.println("[Inverser] modifying...");
        for (int count = 0; count < classNodes.getLength(); count++) {
            Element e = (Element) classNodes.item(count);
            if (e.getAttribute("temp_id").equals(tbl.getTempId())) {
                e.setAttribute("table", tbl.getTableName());
            }
        }
        
        System.out.println("[Inverser] done!");
        
    }
    
    public void updateHbmProperty(Column tbl, Document doc) {
        if (tbl == null || doc == null) return;
        Element rootNode = doc.getDocumentElement();
        
        // column means in mySQL, it may be property tag
        NodeList columnNodes = rootNode.getElementsByTagName("property");
        
        System.out.println("[Inverser] modifying [" + tbl.getRefXml() + "]");
        for (int count = 0; count < columnNodes.getLength(); count++) {
            Element e = (Element) columnNodes.item(count);
            if (e.getAttribute("temp_id").equals(tbl.getTempId())) {
                if (tbl.getType() != null) {
                    e.setAttribute("type", tbl.getType());
                }
                if (tbl.getLength() != null) {
                    e.setAttribute("length", tbl.getLength());
                }
                
                Element childCol = (Element) e.getElementsByTagName("column").item(0);
                if (tbl.getName() != null) {
                    childCol.setAttribute("name", tbl.getName());
                }
                if (tbl.isNotNull()) {
                    childCol.setAttribute("not-null", "true");
                } else {
                    childCol.setAttribute("not-null", "false");
                }
                
            }
        }
        System.out.println("[Inverser] done!");
        
    }
    
    public void updateHbmId(Column col, Document doc) {
        if (col == null || doc == null) return;
        Element rootNode = doc.getDocumentElement();
        
        // column means in mySQL, it may be property tag
        Element idNode = (Element) rootNode.getElementsByTagName("id").item(0);
        
        System.out.println("[Inverser] modifying [" + col.getRefXml() + "]");
        if (idNode.getAttribute("temp_id").equals(col.getTempId())) {
            if (col.getType() != null) {
                idNode.setAttribute("type", col.getType());
            }
            if (col.getLength() != null) {
                idNode.setAttribute("length", col.getLength());
            }
            
            Element childCol = (Element) idNode.getElementsByTagName("column").item(0);
            if (col.getName() != null) {
                childCol.setAttribute("name", col.getName());
            }
            if (col.isNotNull()) {
                childCol.setAttribute("not-null", "true");
            } else {
                childCol.setAttribute("not-null", "false");
            }
            
            Element childGen = (Element) idNode.getElementsByTagName("generator").item(0);
            if (col.isAutoIncrement()) {
                childGen.setAttribute("class", "increment");
            } else {
                childGen.setAttribute("class", "assigned");
            }
        }
        
        System.out.println("[Inverser] done!");
    }
    
    public void saveXml(String xmlPath, Document doc) {
        try {
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xPath = xFactory.newXPath();
            XPathExpression exprs = xPath.compile("//*[@temp_id]");
            
            NodeList nodeList = (NodeList) exprs.evaluate(doc, XPathConstants.NODESET);
            for (int count = 0; count < nodeList.getLength(); count++) {
                Element e = (Element) nodeList.item(count);
                e.removeAttribute("temp_id");
            }
            
            XMLSerializer serializer = new XMLSerializer();
            serializer.setOutputCharStream(new java.io.FileWriter(xmlPath));
            OutputFormat format = new OutputFormat();
            format.setStandalone(true);
            serializer.setOutputFormat(format);
            serializer.serialize(doc);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e1) {
            e1.printStackTrace();
        }
    }
    
}
