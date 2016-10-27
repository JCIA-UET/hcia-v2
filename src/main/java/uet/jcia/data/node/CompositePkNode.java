package uet.jcia.data.node;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("compositepk")
public class CompositePkNode extends ColumnNode {
	
	private static final long serialVersionUID = 8293790352913297707L;
	private List<ColumnNode> fkList = new ArrayList<>();

	public List<ColumnNode> getFkList() {
		return fkList;
	}
	
	public void setFkList(List<ColumnNode> fkList) {
		this.fkList = fkList;
	}
}
