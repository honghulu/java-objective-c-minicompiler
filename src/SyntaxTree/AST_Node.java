package SyntaxTree;
import Scanners.Scanner;
import Scanners.Scanner.TokenType;

public abstract class AST_Node {
	public Scanner.TokenType type;
	
	protected AST_Node leftChild;
	protected AST_Node rightChild;
	
	public void add_leftChild(AST_Node node) {
		leftChild = node;
	}
	
	public void add_rightChild(AST_Node node) {
		rightChild = node;
	}
	
	public AST_Node get_leftChild() {
		return leftChild;
	}
	
	public AST_Node get_rightChild() {
		return rightChild;
	}
	
	public boolean tokenTypeEqual(Scanner.TokenType givenType) {
		return type.equals(givenType);
	}
	
	public String toString() {
		String left_str = "";
		String right_str = "";
		
		if (leftChild != null) {
			left_str = "" + leftChild.getClass();
		}
		if (rightChild != null) {
			right_str = "" + rightChild.getClass();
		}
		return type + "? left child: " + left_str + ", right child: " + right_str;
	}
	
}
