package SyntaxTree;
import Scanners.Scanner;
import Scanners.Scanner.TokenType;

public class AST_NUM extends AST_EXPR {
	private int digitVal;
	private int offset;
	
	public AST_NUM(int id, int value) {
		super.type = Scanner.TokenType.NUM;
		tempID = id;
		digitVal = value;
	}
	
	public int getDigitVal() {
		return digitVal;
	}
	
	public void setOffset(int given_offset) {
		offset = given_offset + tempID;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public String selfDeclaration() {
		return "temp" + tempID + " = " + digitVal + "\n";
	}
	
}
