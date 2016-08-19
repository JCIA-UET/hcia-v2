package cuong;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.utils.HibernateHelper;

public class TestHibernateHelper {
	public static void main(String[] args) {
		List<String> hbmList = new ArrayList<>();
		hbmList.add("I:\\Workspace\\hcia-v2\\temp\\upload\\Category.hbm.xml");
//		hbmList.add("I:\\Workspace\\hcia-v2\\temp\\upload\\OrderItem.hbm.xml");
		
		String ddl = HibernateHelper.generateDdl(hbmList);
		System.out.println(ddl);
	}
}
