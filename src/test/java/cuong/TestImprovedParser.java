package cuong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import uet.jcia.data.node.ColumnNode;
import uet.jcia.data.node.TableNode;
import uet.jcia.data.node.TreeNode;
import uet.jcia.model.parser.HbmParser;
import uet.jcia.utils.TreeDataHelper;

public class TestImprovedParser {

    public static void main(String[] args) {
        List<String> xmlList = new ArrayList<>();
        xmlList.add("src/main/resources/sample-data/Address.hbm.xml");
        xmlList.add("src/main/resources/sample-data/Category.hbm.xml");
        xmlList.add("src/main/resources/sample-data/Customer.hbm.xml");
        xmlList.add("src/main/resources/sample-data/Order.hbm.xml");
        xmlList.add("src/main/resources/sample-data/OrderItem.hbm.xml");
        xmlList.add("src/main/resources/sample-data/Product.hbm.xml");
//        
        HbmParser parser = new HbmParser();
        
        TreeNode rootNode = parser.parse(xmlList);
        
        System.out.println(rootNode);
        
//        ColumnNode column = (ColumnNode) TreeDataHelper.findTreeNodeById(rootNode, 1);
//        Element element = column.getLinkedElement();
//        System.out.println(element);
        
//        HashMap<String, Document> mapper = parser.getCachedDocument();
//        Document doc = (Document)mapper.get("src/main/resources/sample-data/Address.hbm.xml");
//        Element e = TreeDataHelper.findElementById(mapper, 0);
//        System.out.println(e);
    }
}
