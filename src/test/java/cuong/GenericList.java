package cuong;

import java.util.ArrayList;
import java.util.List;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.TreeNode;

public class GenericList {

    static List<TreeNode> list = new ArrayList<>();
    
    static {
        ColumnNode child = new ColumnNode();
        list.add(child);
    }
}
