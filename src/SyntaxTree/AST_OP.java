package SyntaxTree;
import Scanners.Scanner;
import Scanners.Scanner.TokenType;

public class AST_OP extends AST_EXPR {
	private int offset;
	
	public AST_OP(Scanner.TokenType type, String symbol) {
		super.type = type;
		set_str_repr(symbol);
	}
	
	public void setOffset(int given_offset) {
		offset = given_offset + tempID;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public String selfDeclaration() {
		return get_str_repr() + " = " + get_leftChild() + " "
				+ get_str_repr() + " " + get_rightChild() + "\n";
	}
}
