package cuong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uet.jcia.entities.Table;
import uet.jcia.model.DeprecatedParser2;

public class TestParser {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        List<String> xmlList = new ArrayList<>();
        xmlList.add("src/main/resources/sample-data/Address.hbm.xml");
//        xmlList.add("src/main/resources/sample-data/Category.hbm.xml");
//        xmlList.add("src/main/resources/sample-data/Customer.hbm.xml");
//        xmlList.add("src/main/resources/sample-data/Order.hbm.xml");
//        xmlList.add("src/main/resources/sample-data/OrderItem.hbm.xml");
//        xmlList.add("src/main/resources/sample-data/Product.hbm.xml");
        
        DeprecatedParser2 p = new DeprecatedParser2();
        List<Table> resultList = p.parseXmlList(xmlList);
        
        for (Table t : resultList) {
            System.out.println(t);
        }
        
        
//        for (String xmlLink : xmlList) {
//            File fXmlFile = new File(xmlLink);
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            
//            System.out.println("[Parser] parsing [" + xmlLink + "]....");
//            Document doc = dBuilder.parse(fXmlFile);
//            doc.getDocumentElement().normalize();
//            System.out.println("[Parser] parsed....");
//        }
    
    }        
}
