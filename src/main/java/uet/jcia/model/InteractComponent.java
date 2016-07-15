package uet.jcia.model;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.entities.Table;

public class InteractComponent {
	private static final InteractComponent intance = new InteractComponent();
	
	private List<Table> listTable ;
	
	private InteractComponent(){
		listTable = new ArrayList<>();
	}
	
	public static InteractComponent getIntance(){
		return intance;
	}
	public List<Table> getListTable(){
		return listTable;
	}
	public void setListTable(String zipFile){
		Scanner scanner = new Scanner();
		List<String> listLinkXml = scanner.searchAndExtractXmlFile(zipFile);
		Parser parser = new Parser();
		listTable = parser.parseAllToListTable(listLinkXml);
	}

}
