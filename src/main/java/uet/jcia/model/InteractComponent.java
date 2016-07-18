package uet.jcia.model;

import java.util.List;

import uet.jcia.entities.Table;

public class InteractComponent {
	private static final InteractComponent intance = new InteractComponent();
	
	private List<Table> listTable ;
	
	private InteractComponent(){
	}
	
	public static InteractComponent getIntance(){
		return intance;
	}
	public List<Table> getListTable(){
		return listTable;
	}
	public void setListTable(List<Table> tablesList){
		InteractComponent ic = InteractComponent.getIntance();
		ic.listTable = tablesList;
	}

}
