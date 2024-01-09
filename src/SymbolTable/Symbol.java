package SymbolTable;

import Scanners.Scanner;

public class Symbol {
	public enum VariableType{
		INT, VOID
	}
	private Scanner.Token typeToken;
	private VariableType type;
	private String name;
	
	public Symbol(Scanner.Token givenTypeToken, String tokenVal) {
		typeToken = givenTypeToken;
		type = matchType(givenTypeToken);
		name = tokenVal;
	}
	
	private VariableType matchType(Scanner.Token token) {
		VariableType varType = null;
		
		if (token.tokenType.equals(Scanner.TokenType.INT)) {
			varType = VariableType.INT;
		} else if (token.tokenType.equals(Scanner.TokenType.VOID)) {
			varType = VariableType.VOID;
		}
		return varType;
	}
	
	public boolean isLocal() {
		return false;
	}
	
	public VariableType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public int getOffset() {
		return -1;
	}
	
	public Scanner.Token getTypeToken(){
		return typeToken;
	}
}
