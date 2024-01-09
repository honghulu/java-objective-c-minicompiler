package SyntaxTree;

import Scanners.Scanner;
import SymbolTable.Symbol;

public class AST_VAR_DECL extends AST_Node {
	private Scanner.TokenType idtype;
	private Scanner.Token idToken;
	
	public AST_VAR_DECL(Scanner.Token typeToken, Scanner.Token idToken) {
		super();
		idtype = typeToken.tokenType;
		this.idToken = idToken;
	}
	
	public String getName() {
		return idToken.tokenVal;
	}
}
