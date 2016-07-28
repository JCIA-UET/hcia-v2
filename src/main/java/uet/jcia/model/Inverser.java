package uet.jcia.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uet.jcia.entities.Column;
import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.MTORelationshipNode;
import uet.jcia.entities.OTMRelationshipNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.Relationship;
import uet.jcia.entities.Table;
import uet.jcia.entities.TableNode;
import uet.jcia.entities.TreeNode;
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
    
    public void updateTable(TreeNode tableNode) {
        if (!(tableNode instanceof TableNode)) {
            return ;
        }
        
        try {
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element rootElement = inverseHbmRoot((TableNode)tableNode, doc);
            doc.appendChild(rootElement);
            
            saveXml(((TableNode) tableNode).getXmlPath(), doc);
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public Element inverseHbmRoot(TableNode tableNode, Document doc) {
        Element hibernateMapping = doc.createElement("hibernate-mapping");

        Element clazz = doc.createElement("class");
        clazz.setAttribute("name", tableNode.getClassName());
        clazz.setAttribute("table", tableNode.getTableName());
        hibernateMapping.appendChild(clazz);
        
        for (TreeNode child : tableNode.getChilds()) {
            if (child instanceof PrimaryKeyNode) {
                PrimaryKeyNode pk = (PrimaryKeyNode) child;
                Element id = doc.createElement("id");
                id.setAttribute("name", pk.getJavaName());
                id.setAttribute("type", Mappers.getSqltoHbm(pk.getDataType()));
                clazz.appendChild(id);
                
                Element column = doc.createElement("column");
                column.setAttribute("name", pk.getColumnName());
                if (pk.getLength() != 0) {
                    column.setAttribute("length", Long.toString(pk.getLength()));
                }
                id.appendChild(column);
                
                Element generator = doc.createElement("generator");
                String generatorClass = "assigned";
                if (pk.isAutoIncrement()) {
                    generatorClass = "increment"; 
                }
                generator.setAttribute("class", generatorClass);
                id.appendChild(generator);
                
            } else if (child instanceof ColumnNode) {
                ColumnNode field = (ColumnNode) child;
                if (!field.isForeignKey()) {
                    Element property = doc.createElement("property");
                    property.setAttribute("name", field.getJavaName());
                    property.setAttribute("type", Mappers.getSqltoHbm(field.getDataType()));
                    clazz.appendChild(property);
                    
                    Element column = doc.createElement("column");
                    column.setAttribute("name", field.getColumnName());
                    if (field.getLength() != 0) {
                        column.setAttribute("length", Long.toString(field.getLength()));
                    }
                    property.appendChild(column);
                }
            } else if (child instanceof MTORelationshipNode) {
                MTORelationshipNode relationship = (MTORelationshipNode) child;
                Element manyToOne = doc.createElement("many-to-one");
                manyToOne.setAttribute("name", relationship.getJavaName());
                manyToOne.setAttribute("class", relationship.getReferTable().getClassName());
                clazz.appendChild(manyToOne);
                
                Element column = doc.createElement("column");
                column.setAttribute("name", relationship.getReferColumn().getColumnName());
                manyToOne.appendChild(column);
            } else if (child instanceof OTMRelationshipNode) {
                OTMRelationshipNode relationship = (OTMRelationshipNode) child;
                Element set = doc.createElement("set");
                set.setAttribute("name", relationship.getJavaName());
                set.setAttribute("table", relationship.getReferTable().getTableName());
                clazz.appendChild(set);
                
                Element key = doc.createElement("key");
                set.appendChild(key);
                Element column = doc.createElement("column");
                column.setAttribute("name", relationship.getForeignKey().getColumnName());
                key.appendChild(column);
                
                Element oneToMany = doc.createElement("one-to-many");
                oneToMany.setAttribute("class", relationship.getReferTable().getClassName());
                set.appendChild(oneToMany);
            }
        }
        
        return hibernateMapping;
    }
    
    public void saveXml(String xmlPath, Document doc) {
        try {
//            XPathFactory xFactory = XPathFactory.newInstance();
//            XPath xPath = xFactory.newXPath();
//            XPathExpression exprs = xPath.compile("//*[@" + DeprecatedParser2.HBM_ATT_TEMP_ID + "]");
//            
//            NodeList nodeList = (NodeList) exprs.evaluate(doc, XPathConstants.NODESET);
//            for (int count = 0; count < nodeList.getLength(); count++) {
//                Element e = (Element) nodeList.item(count);
//                e.removeAttribute(DeprecatedParser2.HBM_ATT_TEMP_ID);
//            }
            
//            XMLSerializer serializer = new XMLSerializer();
//            serializer.setOutputCharStream(new java.io.FileWriter(xmlPath));
//            OutputFormat format = new OutputFormat();
//            format.setStandalone(true);
//            serializer.setOutputFormat(format);
//            serializer.serialize(doc);
            
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            DOMImplementation domImpl = doc.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("doctype",
                    "-//Hibernate/Hibernate Mapping DTD 3.0//EN",
                    "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
                
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlPath));

            transformer.transform(source, result);
            
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
