package uet.jcia.model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import uet.jcia.entities.TableNode;
import uet.jcia.utils.JsonHelper;

public class JavaParser {
    
    public static final String SAMPLE_TEST =
            "I:\\Dropbox\\Workspace\\hcia-v2\\src\\test\\java\\cuong\\data\\javasample\\Customer.java";
    
    public static void main(String[] args) throws IllegalArgumentException, IOException {
        JavaParser parser = new JavaParser();
        char[] source = file2CharArr(SAMPLE_TEST);
        parser.parseSource(source);
    }
    
    public TableNode parseSource(char[] source) {
        /* initialize abstract tree */
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);

        astParser.setSource(source);
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        
        HASTVisitor hast = new HASTVisitor();
        cu.getRoot().accept(hast);
        
        TableNode table = hast.getTable();
        System.out.println(JsonHelper.toJsonString(table));
        return table;
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
