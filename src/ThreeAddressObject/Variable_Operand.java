package ThreeAddressObject;

public class Variable_Operand extends Operand {
	protected String reference;
	
	public Variable_Operand(String strVal) {
		reference = strVal;
	}
	
	public String toCString() {
		return reference;
	}
	
	public String toString() {
		return reference;
	}
}
