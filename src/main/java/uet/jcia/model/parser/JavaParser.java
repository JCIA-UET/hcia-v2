package uet.jcia.model.parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import uet.jcia.data.node.ColumnNode;
import uet.jcia.data.node.CompositePkNode;
import uet.jcia.data.node.MTORelationshipNode;
import uet.jcia.data.node.OTMRelationshipNode;
import uet.jcia.data.node.PrimaryKeyNode;
import uet.jcia.data.node.RelationshipNode;
import uet.jcia.data.node.RootNode;
import uet.jcia.data.node.TableNode;
import uet.jcia.data.node.TreeNode;
import uet.jcia.utils.Helper;
import uet.jcia.utils.JsonHelper;

public class JavaParser implements Parser{
    
    public static final String[] SAMPLE_TEST =
    {
            "F:\\Workspace\\hcia-v2\\src\\test\\java\\cuong\\data\\javasample\\Payment.java"
    };

    private HASTVisitor visitor;
    
    public JavaParser() {
        visitor = new HASTVisitor();
    }
    
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        JavaParser parser = new JavaParser();
        RootNode root = parser.parse(Arrays.asList(SAMPLE_TEST));
        System.out.println(JsonHelper.toJsonString(root));
    }
    
    public RootNode parseSingleFile(String path) {
        List<String> javaList = new ArrayList<>();
        javaList.add(path);
        return parse(javaList);
    }
    
    public RootNode parse(List<String> sourcePaths) {
        RootNode root = new RootNode();
        List<TreeNode> children = new ArrayList<>();
        try {
            for (String sourcePath : sourcePaths) {
                char[] source = file2CharArr(sourcePath);
                TableNode table = parseSource(source);
                if (table != null) {
                    table.setJavaPath(sourcePath);
                    String xmlPath = sourcePath.replace(".java", ".hbm.xml");
                    table.setXmlPath(xmlPath);
                    children.add(table);
                }
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        root.setChilds(children);
        //!-------------------------------------------
        resolveRelationship(root);
        resolveCompositeId(root);
        return root;
    }
    
    /**
     * 
     * @param root
     */
    public void resolveRelationship(RootNode root) {
        for (TreeNode table : root.getChilds()) {
            List<ColumnNode> fks = new ArrayList<>();
            for (TreeNode child : table.getChilds()) {
                if (child instanceof RelationshipNode) {
                    RelationshipNode relationship = (RelationshipNode) child;
                    String referClass = relationship.getReferTable().getClassName();
                    TableNode referTable = visitor.getTableNodeByClassName(referClass);
                    if (referTable != null) {
                        TableNode copiedTable = (TableNode) Helper.deepClone(referTable);
                        copiedTable.setChilds(null);
                        relationship.setReferTable(copiedTable);
                    }
                }
                if (child instanceof OTMRelationshipNode) {
                    OTMRelationshipNode otm = (OTMRelationshipNode) child;
                    PrimaryKeyNode pk = null;
                    for (TreeNode child2 : table.getChilds()) {
                        if (child2 instanceof PrimaryKeyNode) {
                            pk = (PrimaryKeyNode) Helper.deepClone(child2);
                        }
                    }
                    if (pk != null)
                        otm.setForeignKey(pk);
                } else if (child instanceof MTORelationshipNode) {
                    MTORelationshipNode mto = (MTORelationshipNode) child;
                    String referClass = mto.getReferTable().getClassName();
                    TableNode referTable = visitor.getTableNodeByClassName(referClass);
                    if (referTable != null) {
                        for (TreeNode child2 : referTable.getChilds()) {
                            if (child2 instanceof PrimaryKeyNode) {
                                PrimaryKeyNode referColumn = (PrimaryKeyNode) Helper.deepClone(child2);
                                mto.setReferColumn(referColumn);
                                ColumnNode fk = generateFkFromPk(referColumn);
                                fk.setTempId(visitor.generateTempId());
                                if (mto.getForeignKey().getColumnName() != null)
                                    fk.setColumnName(mto.getForeignKey().getColumnName());
                                mto.setForeignKey(fk);
                                fks.add(fk);
                                break;
                            }
                        }
                    }
                }
            }
            table.getChilds().addAll(fks);
        }
    }
    
    public void resolveCompositeId(RootNode root) {
        for (TreeNode table : root.getChilds()) {
            for (TreeNode child : table.getChilds()) {
                if (child instanceof CompositePkNode) {
                    CompositePkNode cp = (CompositePkNode) child;
                    List<ColumnNode> tempFks = new ArrayList<>();
                    for (ColumnNode fk : cp.getFkList()) {
                        ColumnNode compositeIdColumn = getColumnNodeByName(table, fk.getColumnName());
                        if (compositeIdColumn != null) {
                            compositeIdColumn.setPrimaryKey(true);
                            tempFks.add(compositeIdColumn);
                        }
                    }
                    cp.setFkList(tempFks);
                }
            }
        }
    }
    
    private ColumnNode getColumnNodeByName(TreeNode table, String columnName) {
        for (TreeNode child : table.getChilds())
            if (child instanceof ColumnNode) {
                ColumnNode column = (ColumnNode) child;
                if (column.getColumnName() != null && column.getColumnName().equals(columnName)) {
                    return column;
                }
            }
        return null;
    }
    
    private ColumnNode generateFkFromPk(PrimaryKeyNode pk) {
        ColumnNode fk = new ColumnNode();
        fk.setColumnName(pk.getColumnName());
        fk.setDataType(pk.getDataType());
        fk.setLength(pk.getLength());
        fk.setForeignKey(true);
        return fk;
    }
    
    public TableNode parseSource(char[] source) {
        /* initialize abstract tree */
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);

        astParser.setSource(source);
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        
        cu.getRoot().accept(visitor);
        return visitor.getTable();
    }
    
    public static char[] file2CharArr(String path)
            throws IllegalArgumentException, IOException {
        
        File file = new File(path);
        if (!file.exists() || !file.isFile()) { 
            throw new IllegalArgumentException("Invalid input file");
        }
        
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"));
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            content.append(line).append("\n");
        }
        br.close();
        return content.toString().toCharArray();
    }
}
