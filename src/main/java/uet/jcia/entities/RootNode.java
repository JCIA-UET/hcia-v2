package uet.jcia.entities;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("rootnode")
public class RootNode extends TreeNode {
	private static final long serialVersionUID = 1L;

	@Override
    public String toString() {
        return super.toString();
    }
}
