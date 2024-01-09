package Exceptions;

public class AmbiguousVariableException extends Exception {
	String diy;
	
	public AmbiguousVariableException(String givenType) {
		super();
		diy = givenType;
	}

	public String toString() {
		return "Error: Ambiguous " + diy + " Names";
	}
}
