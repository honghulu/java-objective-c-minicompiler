package SymbolTable;

import Scanners.Scanner;

public class funcSymbol extends Symbol {
	private int stack_space;

	public funcSymbol(Scanner.Token givenTypeToken, String tokenVal) {
		super(givenTypeToken, tokenVal);
		stack_space = 0;
	}
	
	public void setSpace(int predict_space) {
		stack_space = predict_space;
	}
	
	public int getSpace() {
		return stack_space;
	}

}
