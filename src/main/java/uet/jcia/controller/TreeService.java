//package uet.jcia.controller;
//
//import java.util.List;
//
//import javax.faces.bean.ApplicationScoped;
//import javax.faces.bean.ManagedBean;
//
//import org.primefaces.model.DefaultTreeNode;
//import org.primefaces.model.TreeNode;
//
//import uet.jcia.entities.Column;
//import uet.jcia.entities.Table;
//import uet.jcia.model.CoreAPI;
//
//
//@ManagedBean(name = "treeService")
//@ApplicationScoped
//public class TreeService {
//	public TreeNode createTable(){
//		CoreAPI inter = CoreAPI.getIntance();
//		List<Table> list = inter.getListTable();
//		
//		TreeNode root = new DefaultTreeNode("root",null) ;
//		
//		if(list == null) {
//			return root;
//		}
//		else {
//			for(Table table : list){
//				TreeNode tableNode = new DefaultTreeNode("table",table,root);
//				List<Column> listColumn = table.getListColumn();
//				for(Column column:listColumn){
//					tableNode.getChildren().add(new DefaultTreeNode("column",column,tableNode));
//				}
//			}
//		}
//		
//		return root;
//	}
//}