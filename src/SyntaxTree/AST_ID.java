package SyntaxTree;
import Scanners.Scanner;
import Scanners.Scanner.Token;
import SymbolTable.Symbol;
import SymbolTable.varSymbol;

public class AST_ID extends AST_EXPR {
	private Scanner.Token id;
	private Symbol idVal;

	public AST_ID(Scanner.Token name) {
		super.type = name.tokenType;
		id = name;
		str_repr = name.tokenVal;
	}
	
	public void addValue(Symbol value) {
		idVal = value;
	}
	
	public boolean isLocal() {
		return idVal.isLocal();
	}
	
	public int getOffset() {
		return idVal.getOffset();
	}
	
	public Scanner.Token getID(){
		return id;
	}
	
	public Symbol getInfo() {
		return idVal;
	}
}
