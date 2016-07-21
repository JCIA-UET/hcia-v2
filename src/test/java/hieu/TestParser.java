package hieu;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.model.Parser;

public class TestParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parser test = new Parser();
		List<String> list = new ArrayList<>();
		list.add("src/main/resources/Sinhvien.hbm.xml");
		list.add("src/main/resources/Customer.hbm.xml");
		System.out.println(test.parseXmlList(list));
	}

}
