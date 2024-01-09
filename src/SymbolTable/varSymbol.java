package SymbolTable;

import Scanners.Scanner;

public class varSymbol extends Symbol {
	public enum VariableScope{
		LOCAL, GLOBAL
	}
	private VariableScope scope;
	private int offset;

	public varSymbol(Scanner.Token givenTypeToken, String tokenVal, int curOffset, VariableScope varScope) {
		super(givenTypeToken, tokenVal);
		offset = curOffset;
		scope = varScope;
	}
	
	public boolean isLocal() {
		return scope.equals(VariableScope.LOCAL);
	}
	
	public int getOffset() {
		return offset;
	}

}
