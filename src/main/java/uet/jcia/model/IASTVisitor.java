package uet.jcia.model;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class IASTVisitor extends ASTVisitor {
	
	@Override
	public boolean visit(TypeDeclaration node) {
		return super.visit(node);
	}
}
