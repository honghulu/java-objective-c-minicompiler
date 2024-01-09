package ThreeAddressObject;

public class Assign_ThreeAddrObject extends ThreeAddrObject {

	public Assign_ThreeAddrObject() {
		super();
	}
	
	// src1 is constant
	public void set_src1(int strVal) {
		src1 = new Constant_Operand(strVal);
	}
		
	// src1 is local var
	public void set_src1(String strVal, int given_offset){
		src1 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// src1 is global var
	public void set_src1(String strVal){
		src1 = new Global_Variable_Operand(strVal);
	}
	
	// dest is local var
	public void set_dest(String strVal, int given_offset) {
		dest = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// dest is global var
	public void set_dest(String strVal) {
		dest = new Global_Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return "r1 = " + src1.toCString() + ";\n" +
				dest.toCString() + " = r1;\n";
	}
	
	public String toString() {
		return dest.toString() + " = " + src1.toString() + "\n";
	}
}
