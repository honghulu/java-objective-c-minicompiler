
package ThreeAddressObject;

public class FunctionCall_ThreeAddrobject extends ThreeAddrObject {
	public FunctionCall_ThreeAddrobject() {
		super();
	}
	
	// src1 is constant
	public void set_src1(int strVal) {
		src1 = new Constant_Operand(strVal);
	}
	
	// src2 is speical var
	public void set_src2(String strVal) {
		src2 = new Variable_Operand(strVal);
	}
	
	// dest is speical var
	public void set_dest(String strVal) {
		dest = new Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return "ra = &&" + src2.toCString() + ";\n" +
				"goto " + dest.toCString() + ";\n" +
				src2.toCString() + ":\n" +
				"sp = sp + " + src1.toCString() + ";\n";
	}
	
	public String toString() {
		return "call " + dest.toString() + ", " + src1.toString() + "\n";
	}
}