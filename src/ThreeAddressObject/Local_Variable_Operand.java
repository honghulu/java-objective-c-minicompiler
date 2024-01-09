package ThreeAddressObject;

public class Local_Variable_Operand extends Variable_Operand {
	private int offset;

	public Local_Variable_Operand(String strVal, int curOffset) {
		super(strVal);
		offset = curOffset;
	}
	
	public String toCString() {
		return offset > 0 ? "*(fp-" + offset + ")" : "*(fp+" + -offset + ")";
	}
}
