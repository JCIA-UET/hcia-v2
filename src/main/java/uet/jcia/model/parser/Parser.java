package uet.jcia.model.parser;

import java.util.List;

import uet.jcia.data.node.TreeNode;

public interface Parser {
    TreeNode parse(List<String> inputFiles);
    TreeNode parseSingleFile(String path);
}
