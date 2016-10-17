package cuong;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.utils.HibernateHelper;

public class TestHibernateHelper {
	public static void main(String[] args) {
		List<String> hbmList = new ArrayList<>();
		hbmList.add("F:\\Workspace\\hcia-v2\\temp\\upload-src-20160919-204643\\classicmodels\\Productline.hbm.xml");
		
		String ddl = HibernateHelper.generateDdl(hbmList);
		System.out.println(ddl);
	}
}
