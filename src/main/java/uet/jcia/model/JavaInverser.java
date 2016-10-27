package uet.jcia.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;

import uet.jcia.data.node.ColumnNode;
import uet.jcia.utils.FileManager;
import uet.jcia.utils.Helper;

import uet.jcia.model.parser.HASTVisitor;

public class JavaInverser {
	
	
	HASTVisitor visitor = new HASTVisitor();
	List<CompilationUnit> cuList = new ArrayList<>();
	
	public static void main(String[] args) throws IllegalArgumentException, IOException, JavaModelException {
		JavaInverser inverser = new JavaInverser();
		inverser.getCompilationUnitList();
		for (CompilationUnit cu : inverser.cuList) {
			System.out.println(cu);
		}
		
		ColumnNode column = new ColumnNode();
		
	}
	
	public void rewriteColumn(ColumnNode column) {
	}
	
	private CompilationUnit parse(String path) throws IllegalArgumentException, IOException {
		char[] src = Helper.file2CharArr(path); 
		
        ASTParser astParser = ASTParser.newParser(AST.JLS8);
        Map options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        astParser.setCompilerOptions(options);
        astParser.setSource(src);
        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        cu.getRoot().accept(visitor);
        return cu;
	}
	
	public void getCompilationUnitList() throws JavaModelException, IllegalArgumentException, IOException {
		FileManager fm = new FileManager();
		List<String> javaList = new ArrayList<>();
		fm.findFiles("I:/Workspace/hcia-v2/temp/upload-src-20160815-145952", ".*\\.java", javaList);
		
		for (String path: javaList) {
			CompilationUnit cu = parse(path);
			cuList.add(cu);
		}
	}

}
