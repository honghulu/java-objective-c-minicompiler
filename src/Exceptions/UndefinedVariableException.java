package Exceptions;

public class UndefinedVariableException extends Exception{
	
	public UndefinedVariableException() {
		super();
	}
	
	public String toString() {
		return "Undefined variable";
	}
}
