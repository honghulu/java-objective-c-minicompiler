package SyntaxTree;

import Scanners.Scanner;

public class AST_PARAM extends AST_Node {
	private Scanner.TokenType idtype;
	private Scanner.Token idToken;
	
	public AST_PARAM(Scanner.Token typeToken, Scanner.Token idToken) {
		super();
		idtype = typeToken.tokenType;
		this.idToken = idToken;
	}
	
	public String getName() {
		return idToken.tokenVal;
	}
}
