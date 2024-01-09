package ThreeAddressObject;

public class ControlFlow_ThreeAddrObject extends ThreeAddrObject {
	private String instr_repr;

	public ControlFlow_ThreeAddrObject(String symbol_str) {
		super();
		str_repr = symbol_str;
		instr_repr = match(symbol_str);
	}
	
	private String match(String symbol_str) {
		String new_repr = null;
		
		if (symbol_str.equals("!=")) {
			new_repr = "IF_NE";
		} else if (symbol_str.equals("==")) {
			new_repr = "IF_EQ";
		} else if (symbol_str.equals("<=")) {
			new_repr = "IF_LTE";
		} else if (symbol_str.equals(">=")) {
			new_repr = "IF_GTE";
		} else if (symbol_str.equals("<")) {
			new_repr = "IF_LT";
		} else if (symbol_str.equals(">")) {
			new_repr = "IF_GT";
		}
		return new_repr;
	}
	
	// src1 is global var
	public void set_src1(String strVal){
		src1 = new Global_Variable_Operand(strVal);
	}
	
	// src1 is local var
	public void set_src1(String strVal, int given_offset){
		src1 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// src2 is global var
	public void set_src2(String strVal) {
		src2 = new Global_Variable_Operand(strVal);
	}
	
	// src2 is local var
	public void set_src2(String strVal, int given_offset) {
		src2 = new Local_Variable_Operand(strVal, given_offset);
	}
	
	// dest is special var
	public void set_dest(String strVal) {
		dest = new Variable_Operand(strVal);
	}
	
	public String toCAssembly() {
		return "r1 = " + src1.toCString() + ";\n" +
				"r2 = " + src2.toCString() + ";\n" +
				"if(r1 " + str_repr + " r2) goto " +
				dest.toCString() + ";\n";
	}
	
	public String toString() {
		return instr_repr + ": " + src1.toString() + ", " + src2.toString() + ", " + dest.toString() + "\n";
	}
}
