package SyntaxTree;

import Scanners.Scanner;

public class AST_RET extends AST_Node {
	
	public AST_RET() {
		super();
		super.type = Scanner.TokenType.RETURN;
	}
	
	public boolean isEmpty() {
		return leftChild == null && rightChild == null;
	}
}
