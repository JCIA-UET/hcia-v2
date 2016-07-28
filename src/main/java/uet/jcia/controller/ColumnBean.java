package uet.jcia.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.MTORelationshipNode;
import uet.jcia.entities.OTMRelationshipNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.model.CoreAPI;

@ManagedBean
@SessionScoped
public class ColumnBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColumnNode columnNode;

	public ColumnNode getColumnNode() {
		return columnNode;
	}

	public void setColumnNode(ColumnNode columnNode) {
		this.columnNode = columnNode;
	}

	public ColumnBean() {
		columnNode = new ColumnNode();
	}

//	public void save(ColumnNode changeColNode) {
//		System.out.println("Change Col Type:" + changeColNode);
//
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		ExternalContext exContext = facesContext.getExternalContext();
//		HttpSession session = (HttpSession) exContext.getSession(false);
//
//		String sessionid = session.getId();
//		String dirKey = sessionid + "dir";
//		String parsedFileDir = (String) session.getAttribute(dirKey);
//
//		CoreAPI api = new CoreAPI();
//		TreeNode root = api.getParsedData(parsedFileDir);
//
//		List<TreeNode> tables = root.getChilds();
//		for (TreeNode t : tables) {
//			List<TreeNode> colsList = t.getChilds();
//			for (TreeNode col : colsList) {
//				if (!(col instanceof MTORelationshipNode) && !(col instanceof OTMRelationshipNode)) {
//					if (col instanceof PrimaryKeyNode) {
//						PrimaryKeyNode tempPKNode = (PrimaryKeyNode) col;
//
//						if (tempPKNode.getTempId() != changeColNode.getTempId())
//							continue;
//
//						int colIndex = colsList.indexOf(col);
//
//						tempPKNode.setColumnName(changeColNode.getColumnName());
//						tempPKNode.setDataType(changeColNode.getDataType());
//						tempPKNode.setForeignKey(changeColNode.isForeignKey());
//						tempPKNode.setLength(changeColNode.getLength());
//						tempPKNode.setNotNull(changeColNode.isNotNull());
//						tempPKNode.setPrimaryKey(changeColNode.isPrimaryKey());
//						tempPKNode.setAutoIncrement(changedColNode);
//
//						colsList.set(colIndex, tempPKNode);
//					} else if (col instanceof ColumnNode) {
//						ColumnNode tempColNode = (ColumnNode) col;
//						
//						if (tempColNode.getTempId() != changeColNode.getTempId())
//							continue;
//
//						int colIndex = colsList.indexOf(col);
//
//						tempColNode.setColumnName(changeColNode.getColumnName());
//						tempColNode.setDataType(changeColNode.getDataType());
//						tempColNode.setForeignKey(changeColNode.isForeignKey());
//						tempColNode.setLength(changeColNode.getLength());
//						tempColNode.setNotNull(changeColNode.isNotNull());
//						tempColNode.setPrimaryKey(changeColNode.isPrimaryKey());
//						
//						
//
//						colsList.set(colIndex, tempColNode);
//					}
//
//				}
//			}
//		}
//	}

	public void restore(String tempId) {
		System.out.println(tempId);
	}
}
