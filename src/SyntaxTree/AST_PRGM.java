package SyntaxTree;

import Scanners.Scanner;

public class AST_PRGM extends AST_Node{
	private String program_name;
	
	public AST_PRGM(Scanner.Token name) {
		super();
		super.type = name.tokenType;
		program_name = name.tokenVal;
	}
}
