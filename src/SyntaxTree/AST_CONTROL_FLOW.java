package SyntaxTree;

import Scanners.Scanner;

public class AST_CONTROL_FLOW extends AST_STMT{
	
	public AST_CONTROL_FLOW(Scanner.Token name) {
		type = name.tokenType;
	}
	
	public void addChild(AST_EXPR expr, AST_STMT_SEQ stmt_seq) {
		this.add_leftChild(expr);
		this.add_rightChild(stmt_seq);
	}
}
