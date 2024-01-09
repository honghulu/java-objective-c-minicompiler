package SyntaxTree;
import Scanners.Scanner;
import Scanners.Scanner.TokenType;

public class AST_CP extends AST_OP{

	public AST_CP(Scanner.TokenType type, String symbol) {
		super(type, symbol);
	}
	
	public String selfDeclaration() {
		return get_str_repr() + ": " + get_leftChild()
		+ ", " + get_rightChild();
	}

}
