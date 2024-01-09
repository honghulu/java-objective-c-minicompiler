package ThreeAddressObject;

public class ReturnValue_ThreeAddrObject extends ThreeAddrObject {
	public ReturnValue_ThreeAddrObject() {
		super();
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
		return "va = " + dest.toCString() + ";\n";
	}
	
	public String toString() {
		return "return " + dest.toString() + "\n";
	}
}

