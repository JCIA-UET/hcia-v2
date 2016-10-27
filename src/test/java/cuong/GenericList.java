package cuong;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.data.node.ColumnNode;
import uet.jcia.data.node.TreeNode;

public class GenericList {

    static List<TreeNode> list = new ArrayList<>();
    
    static {
        ColumnNode child = new ColumnNode();
        list.add(child);
    }
}
