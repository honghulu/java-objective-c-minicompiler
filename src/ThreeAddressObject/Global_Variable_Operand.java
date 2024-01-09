package ThreeAddressObject;

public class Global_Variable_Operand extends Variable_Operand {

	public Global_Variable_Operand(String strVal) {
		super(strVal);
	}
	
	public String toCString() {
		return reference;
	}
}
