//package cuong;
//
//import java.util.List;
//
//import uet.jcia.entities.Table;
//import uet.jcia.model.Inverser;
//import uet.jcia.model.Parser;
//
//public class TestInverser {
//    
//    public static void main(String argv[]) {
//        Parser parser = new Parser();
//        Inverser inverser = new Inverser();
//        inverser.setParser(parser);
//        String xmlPath = "I:/Workspace/hcia-v2/src/main/resources/sample-data/Address.hbm.xml";
//        List<Table> tableList = parser.parseXml(xmlPath);
//        
//        System.out.println(tableList);
//        
//        inverser.removeNode("I:/Workspace/hcia-v2/src/main/resources/sample-data/Address.hbm.xml", "4");
//        
//        inverser.saveXml(xmlPath, parser.getDocumentByXmlPath(xmlPath));
//    }
//}
