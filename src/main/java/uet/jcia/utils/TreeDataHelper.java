package uet.jcia.utils;

import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.DeprecatedParser2;

public class TreeDataHelper {
    
    public static ColumnNode generateForeignKey(PrimaryKeyNode pk) {
        ColumnNode fk = new ColumnNode();
        
        fk.setTempId(pk.getTempId());
        fk.setColumnName(pk.getColumnName());
        fk.setDataType(pk.getDataType());
        fk.setLength(pk.getLength());
        fk.setPrimaryKey(false);
        fk.setNotNull(true);
        fk.setForeignKey(true);
        
        return fk;
    }

    public static TreeNode findTreeNodeById(TreeNode rootNode, long tempId) {
        if (rootNode == null || rootNode.getTempId() == tempId) {
            return rootNode;
            
        } else if (rootNode.getChilds() == null){
            return null;
            
        } else {
            for (TreeNode childNode : rootNode.getChilds()) {
                if (childNode.getTempId() <= tempId) {
                    TreeNode result = findTreeNodeById(childNode, tempId);
                    if (result != null) return result;
                }
            }
            return null;
        }
    }
    
    public static Element findElementById(Document doc, long tempId) {
        try {
            XPathFactory xFactory = XPathFactory.newInstance();
            XPath xPath = xFactory.newXPath();
            String pattern = "//*[@" + DeprecatedParser2.HBM_ATT_TEMP_ID + "=\"" + Long.toString(tempId) + "\"]";
            XPathExpression exprs = xPath.compile(pattern);
            NodeList nodeList = (NodeList) exprs.evaluate(doc, XPathConstants.NODESET);
            Element result = (Element) nodeList.item(0);
            if (result != null) return result;
            else return null;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Element findElementById(HashMap<String, Document> mapper, long tempId) {
        try {
            for (String key : mapper.keySet()) {
                Document doc = mapper.get(key);
                XPathFactory xFactory = XPathFactory.newInstance();
                XPath xPath = xFactory.newXPath();
                String pattern = "//*[@" + DeprecatedParser2.HBM_ATT_TEMP_ID + "=\"" + Long.toString(tempId) + "\"]";
                XPathExpression exprs = xPath.compile(pattern);
                NodeList nodeList = (NodeList) exprs.evaluate(doc, XPathConstants.NODESET);
                Element result = (Element) nodeList.item(0);
                if (result != null) return result;
            }
            return null;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
