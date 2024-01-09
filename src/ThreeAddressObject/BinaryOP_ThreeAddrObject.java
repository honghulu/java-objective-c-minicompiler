package ThreeAddressObject;

public class BinaryOP_ThreeAddrObject extends ThreeAddrObject {
	
	public BinaryOP_ThreeAddrObject(String symbol_str) {
		super();
		str_repr = symbol_str;
	}
	
	// src1 is local var
	public void set_src1(String strVal, int given_offset){
		src1 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// src1 is global var
	public void set_src1(String strVal){
		src1 = new Global_Variable_Operand(strVal);
	}
	
	// src2 is local var
	public void set_src2(String strVal, int given_offset) {
		src2 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// src2 is global var
	public void set_src2(String strVal) {
		src2 = new Global_Variable_Operand(strVal);
	}
	
	// dest is definitely local var
	public void set_dest(String strVal, int given_offset) {
		dest = new Local_Variable_Operand(strVal, given_offset);
	}
	
	public String toCAssembly() {
		return "r1 = " + src1.toCString() + ";\n" + 
				"r2 = " + src2.toCString() + ";\n" + 
				"r3 = r1 " + str_repr + " r2;\n" +
				dest.toCString() + " = r3;\n";
	}
	
	public String toString() {
		return dest.toString() + " = " + src1.toString() + " " + str_repr + " " + src2.toString() + "\n";
	}
	
}
