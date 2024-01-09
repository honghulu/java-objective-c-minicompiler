package ThreeAddressObject;

public class Constant_Operand extends Operand {
	private int constant;
	
	public Constant_Operand(int intVal) {
		constant = intVal;
	}
	
	public String toCString() {
		return "" + constant;
	}
	
	public String toString() {
		return "" + constant;
	}
}
