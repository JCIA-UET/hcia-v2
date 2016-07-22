package uet.jcia.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uet.jcia.entities.Column;
import uet.jcia.entities.Table;
import uet.jcia.utils.Mappers;

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
    
    public void updateTable(Table tbl, Document doc) {
        if (tbl == null || doc == null) return;
        
        Element rootNode = doc.getDocumentElement();
        NodeList classNodes = rootNode.getElementsByTagName("class");
        
        System.out.println("[Inverser] modifying...");
        for (int i = 0; i < classNodes.getLength(); i++) {
            Element classElement = (Element) classNodes.item(i);
            
            // update table name
            if (classElement.getAttribute("temp_id").equals(tbl.getTempId())) {
                classElement.setAttribute("table", tbl.getTableName());
            }
            
            // update columns
            List<Column> columnList = tbl.getListColumn();
            if (columnList != null) {
                for (Column c : columnList) {
                    Element colElement = getElementByTempId(
                            classElement, c.getTempId());
                    
                    if (colElement.getTagName().equals("id")) {
                        updateHbmId(c, colElement);
                        
                    } else if (colElement.getTagName().equals("property")) {
                        updateHbmProperty(c, colElement);
                    }
                }
            }
            
        }
        
        System.out.println("[Inverser] done!");
        
    }
    
    private Element getElementByTempId(Element rootElement, String tempId) {
        NodeList nodeList = rootElement.getChildNodes();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node childNode = nodeList.item(count);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;
                if (childElement.getAttribute("temp_id") != null &&
                    childElement.getAttribute("temp_id").equals(tempId)) {
                    return childElement;
                }
            }
        }
        return null;
    }
    
    public void updateHbmProperty(Column col, Element propertyElement) {
        if (col.getType() != null) {
            propertyElement.setAttribute("type", Mappers.getSqltohbm(col.getType()));
        }
        if (col.getLength() != null) {
            propertyElement.setAttribute("length", col.getLength());
        }
        
        Element childCol = (Element) propertyElement.getElementsByTagName("column").item(0);
        if (col.getName() != null) {
            childCol.setAttribute("name", col.getName());
        }
        if (col.isNotNull()) {
            childCol.setAttribute("not-null", "true");
        } else {
            childCol.setAttribute("not-null", "false");
        }
    }
    
    public void updateHbmId(Column col, Element idElement) {
        if (col.getType() != null) {
            idElement.setAttribute("type", Mappers.getSqltohbm(col.getType()));
        }
        if (col.getLength() != null) {
            idElement.setAttribute("length", col.getLength());
        }
        
        Element childCol = (Element) idElement.getElementsByTagName("column").item(0);
        if (col.getName() != null) {
            childCol.setAttribute("name", col.getName());
        }
        if (col.isNotNull()) {
            childCol.setAttribute("not-null", "true");
        } else {
            childCol.setAttribute("not-null", "false");
        }
        
        Element childGen = (Element) idElement.getElementsByTagName("generator").item(0);
        if (col.isAutoIncrement()) {
            childGen.setAttribute("class", "increment");
        } else {
            childGen.setAttribute("class", "assigned");
        }
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
