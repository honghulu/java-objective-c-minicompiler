package SyntaxTree;

import Scanners.Scanner;
import SymbolTable.Symbol;

public class AST_FUNC extends AST_Node {
	private String funcName;
	private Symbol funcInfo;

	public AST_FUNC(Scanner.Token name, Symbol sym) {
		funcName = name.tokenVal;
		funcInfo = sym;
	}
	
	public Symbol getInfo() {
		return funcInfo;
	}

}
