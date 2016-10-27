package cuong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uet.jcia.data.node.TreeNode;
import uet.jcia.entities.Table;
import uet.jcia.model.DeprecatedParser2;
import uet.jcia.model.parser.HbmParser;
import uet.jcia.model.parser.Parser;
import uet.jcia.utils.JsonHelper;

public class TestParser {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        List<String> xmlList = new ArrayList<>();
        xmlList.add("temp/upload/vnu/jcia/Address.hbm.xml");
        xmlList.add("temp/upload/vnu/jcia/Category.hbm.xml");
        xmlList.add("temp/upload/vnu/jcia/Customer.hbm.xml");

        Parser p = new HbmParser();
        TreeNode root = p.parse(xmlList);

        System.out.println(JsonHelper.toJsonString(root));
        
    }
}
